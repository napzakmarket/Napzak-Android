package com.napzak.market.remote

import com.napzak.market.remote.model.StompSocketConnectionState
import com.napzak.market.remote.model.StompSocketConnectionState.CONNECTED
import com.napzak.market.remote.model.StompSocketConnectionState.CONNECTING
import com.napzak.market.remote.model.StompSocketConnectionState.DISCONNECTED
import com.napzak.market.remote.model.StompSocketException
import com.napzak.market.remote.model.StompSocketException.ConnectFailed
import com.napzak.market.remote.model.StompSocketException.HeartBeatFailed
import data.remote.BuildConfig.WEBSOCKET_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.frame.FrameBody
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import kotlin.math.pow

interface StompSocketClient {
    val session: StompSessionWithKxSerialization?
    val connectionState: StateFlow<StompSocketConnectionState>
    suspend fun connect(host: String = "/")
    suspend fun disconnect()
    suspend fun <T : Any> subscribe(destination: String, deserializer: KSerializer<T>): Flow<T>
    suspend fun <T : Any> send(destination: String, request: T, serializer: KSerializer<T>)
}

class StompSocketClientImpl @Inject constructor(
    private val okHttpWebSocketClient: OkHttpWebSocketClient,
    private val json: Json
) : StompSocketClient {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val connectMutex = Mutex()
    private var pingJob: Job? = null
    private var pongJob: Job? = null

    private val _connectionState = MutableStateFlow(DISCONNECTED)
    override val connectionState = _connectionState.asStateFlow()

    override var session: StompSessionWithKxSerialization? = null
        private set

    private var connectionRetryCount = AtomicInteger(0)
    private var heartBeatRetryCount = AtomicInteger(0)

    /**
     * 기본 URL과 [host]로 소켓 연결을 시도합니다.
     * - 연결에 성공하면 [StompSocketState.CONNECTED]로 상태가 변경됩니다. 그리고 곧바로 하트비트가 실행됩니다.
     * - 연결에 실패하면 최대 5번까지 재연결을 시도합니다.
     *
     * @throws Exception 5번 이상 재연결에 실패한 경우
     */
    override suspend fun connect(host: String): Unit = connectMutex.withLock {
        try {
            if (_connectionState.value != DISCONNECTED) {
                logSuccess("CONNECT", "이미 소켓이 연결되었습니다.")
                return@withLock
            }
            _connectionState.update { CONNECTING }

            session = StompClient(okHttpWebSocketClient)
                .connect(
                    url = WEBSOCKET_URL,
                    host = host,
                )
                .withJsonConversions(
                    json = json,
                )
            subscribePong()
            sendPing()

            connectionRetryCount.set(0)
            _connectionState.update { CONNECTED }
            logSuccess("CONNECT", "소켓이 연결되었습니다.")
        } catch (e: Exception) {
            handleSocketError(ConnectFailed(e))
        }
    }

    /**
     * 소켓 연결을 해제합니다. 이미 해제된 경우 아무런 동작도 하지 않습니다.
     *
     * 정상적으로 해제되면 graceful disconnect가 이뤄집니다.
     * 이 경우, 마지막으로 보내는 메시지가 성공적으로 소켓의 끝에 도달하는 것을 보장합니다.
     *
     * - 하트비트 작업 취소가 선행됩니다.
     * - 세션 연결 해제 및 자원 해제가 이루어집니다.
     * - [StompSocketState.DISCONNECTED]로 상태가 변경됩니다.
     */
    override suspend fun disconnect(): Unit = connectMutex.withLock {
        if (connectionState.value == DISCONNECTED) {
            logSuccess("DISCONNECT", "이미 소켓이 해제되었습니다.")
            return@withLock
        }
        try {
            /* 소켓 하트비트 작업 취소 */
            pingJob?.cancel()
            pongJob?.cancel()

            /* 세션 연결 해제 및 자원 해제 */
            session?.disconnect()
            session = null

            _connectionState.update { DISCONNECTED }
            logSuccess("DISCONNECT", "소켓 연결이 해제되었습니다.")
        } catch (e: Exception) {
            logError("DISCONNECT", e)
            throw e
        }
    }

    /**
     * 소켓 위 채널을 구독합니다.
     *
     * 구독에 실패하거나, 수신한 메시지에 이상이 있더라도 예외가 발생하지 않습니다.
     * 허나 소켓이 연결되어있지 않다면 예외가 발생합니다.
     *
     * @throws IllegalStateException 소켓이 연결되지 않은 경우
     */
    override suspend fun <T : Any> subscribe(
        destination: String,
        deserializer: KSerializer<T>,
    ): Flow<T> {
        val currentSession = requireNotNull(session) { "소켓이 연결되지 않았습니다." }
        val flow = currentSession
            .subscribe(StompSubscribeHeaders(destination), deserializer)
            .catch { logError("SUBSCRIBE", it) }

        logSuccess("SUBSCRIBE", "소켓 채널이 구독되었습니다. $destination")
        return flow
    }

    /**
     * 메시지를 전송합니다.
     *
     * 메시지를 어떤 채널을 통해 전송했을 때, 정상적으로 전송되었다면 해당 채널을 통해 전송한 메시지를 수신합니다.
     *
     * 메시지 전송을 실패했을 경우엔 예외가 따로 발생하지 않습니다.
     *
     * @throws SerializationException 메시지 직렬화에 실패한 경우
     */
    override suspend fun <T : Any> send(
        destination: String,
        request: T,
        serializer: KSerializer<T>
    ) {
        val message = json.encodeToString(serializer, request)
        session?.send(
            headers = StompSendHeaders(
                destination = destination,
                configure = { contentType = APPLICATION_JSON }
            ),
            body = FrameBody.Text(message),
        )
        logSuccess("SEND", "메시지를 보냈습니다. $message")
    }

    private fun sendPing(destination: String = "/pub/ping") {
        val pingDelay = 30000L
        pingJob = serviceScope.launch {
            try {
                while (session != null && _connectionState.value == CONNECTED) {
                    delay(pingDelay)
                    session?.send(
                        headers = StompSendHeaders(destination = destination),
                        body = null,
                    )
                    logSuccess("HEARTBEAT", "Ping sent")
                }
            } catch (e: Exception) {
                handleSocketError(HeartBeatFailed(e))
            }
        }
    }

    private suspend fun subscribePong(destination: String = "/topic/pong") {
        try {
            val flow = session?.subscribe(
                headers = StompSubscribeHeaders(destination = destination),
            )
            pongJob?.cancel()
            pongJob = serviceScope.launch {
                flow
                    ?.catch { throw it }
                    ?.collect { logSuccess("PONG", "Pong received") }
            }
            logSuccess("HEARTBEAT", "Pong subscribed")
        } catch (e: Exception) {
            handleSocketError(HeartBeatFailed(e))
        }
    }

    private suspend fun handleSocketError(exception: StompSocketException) {
        when (exception) {
            is ConnectFailed -> {
                if (connectionRetryCount.incrementAndGet() > MAX_RETRY_COUNT) {
                    logError("CONNECT", exception)
                    throw exception.cause
                } else {
                    handleConnectionFailure()
                }
            }

            is HeartBeatFailed -> {
                if (heartBeatRetryCount.incrementAndGet() > MAX_RETRY_COUNT) {
                    logError("HEARTBEAT", exception)
                    disconnect()
                    throw exception.cause
                } else {
                    handleHeartBeatFailure()
                }
            }
        }
    }

    private fun handleConnectionFailure() {
        val delay = 2 * 2.0.pow((connectionRetryCount.get() - 1).toDouble()).toLong()
        val jitterMillis = (0..delay).random() * 1000
        serviceScope.launch {
            delay(timeMillis = jitterMillis)
            connect(BASE_HOST)
        }
    }

    private suspend fun handleHeartBeatFailure() {
        pingJob?.cancel()
        pongJob?.cancel()
        sendPing()
        subscribePong()
    }

    private fun logSuccess(header: String, message: String) {
        Timber.tag(TAG).d("✅ $header: $message")
    }

    private fun logError(header: String, error: Throwable) {
        Timber.tag(TAG).e("❌ $header: 오류가 발생했습니다.\n$error")
    }

    companion object {
        private const val TAG = "StompSocketClient"
        private const val APPLICATION_JSON = "application/json"
        private const val BASE_HOST = "/"
        private const val MAX_RETRY_COUNT = 5
    }
}
