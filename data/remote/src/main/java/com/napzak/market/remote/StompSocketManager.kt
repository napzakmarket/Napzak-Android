package com.napzak.market.remote

import data.remote.BuildConfig.WEBSOCKET_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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

enum class StompSocketState {
    CONNECTING,
    CONNECTED,
    DISCONNECTED
}

interface StompSocketManager {
    val stompSession: StompSessionWithKxSerialization?
    val stompSocketState: StompSocketState
    suspend fun connect(host: String = "/")
    suspend fun disconnect()
    suspend fun <T : Any> subscribe(destination: String, deserializer: KSerializer<T>): Flow<T>
    suspend fun <T : Any> send(destination: String, request: T, serializer: KSerializer<T>)
}

class StompSocketManagerImpl @Inject constructor(
    private val okHttpWebSocketClient: OkHttpWebSocketClient,
    private val json: Json
) : StompSocketManager {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val connectMutex = Mutex()

    private var pingJob: Job? = null
    private var pongJob: Job? = null

    override var stompSocketState: StompSocketState = StompSocketState.DISCONNECTED
        private set
    override var stompSession: StompSessionWithKxSerialization? = null
        private set

    private var retryCount = AtomicInteger(0)

    /**
     * 기본 URL과 [host]로 소켓 연결을 시도합니다.
     * - 연결에 성공하면 [StompSocketState.CONNECTED]로 상태가 변경됩니다. 그리고 곧바로 하트비트가 실행됩니다.
     * - 연결에 실패하면 최대 5번까지 재연결을 시도합니다.
     *
     * @throws Exception 5번 이상 재연결에 실패한 경우
     */
    override suspend fun connect(host: String): Unit = connectMutex.withLock {
        try {
            if (stompSession != null && stompSocketState != StompSocketState.DISCONNECTED) {
                logSuccess("CONNECT", "이미 소켓이 연결되었습니다.")
                return@withLock
            }
            updateStompSocketState(StompSocketState.CONNECTING)
            stompSession = StompClient(webSocketClient = okHttpWebSocketClient)
                .connect(
                    url = WEBSOCKET_URL,
                    host = host,
                ).withJsonConversions(
                    json = json,
                )
            logSuccess("CONNECT", "소켓이 연결되었습니다.")
            retryCount.set(0)
            updateStompSocketState(StompSocketState.CONNECTED)
            subscribePong()
            sendPing()
        } catch (e: Exception) {
            logError("CONNECT", e)
            if (retryCount.get() >= MAX_RETRY_COUNT) throw e
            else handleConnectionFailure()

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
        if (stompSocketState == StompSocketState.DISCONNECTED) {
            logSuccess("DISCONNECT", "이미 소켓이 해제되었습니다.")
            return@withLock
        }
        try {
            /* 소켓 하트비트 작업 취소 */
            pingJob?.cancel()
            pongJob?.cancel()

            /* 세션 연결 해제 및 자원 해제 */
            stompSession?.disconnect()
            stompSession = null

            updateStompSocketState(StompSocketState.DISCONNECTED)
            logSuccess("DISCONNECT", "소켓 연결이 해제되었습니다.")
        } catch (e: Exception) {
            logError("DISCONNECT", e)
            throw e
        }
    }

    /**
     * 채팅방 채널을 구독합니다.
     * @throws IllegalStateException 소켓이 연결되지 않은 경우
     */
    override suspend fun <T : Any> subscribe(
        destination: String,
        deserializer: KSerializer<T>,
    ): Flow<T> {
        val flow =
            stompSession?.subscribe(StompSubscribeHeaders(destination), deserializer)?.catch {
                logError("SUBSCRIBE", it)
            } ?: throw IllegalStateException("소켓이 연결되지 않았습니다.")

        logSuccess("SUBSCRIBE", "소켓 채널이 구독되었습니다. $destination")
        return flow
    }

    /**
     * 메시지를 전송합니다.
     */
    override suspend fun <T : Any> send(
        destination: String,
        request: T,
        serializer: KSerializer<T>
    ) {
        val message = json.encodeToString(serializer, request)
        stompSession?.send(
            headers = StompSendHeaders(
                destination = destination,
                configure = { contentType = APPLICATION_JSON }
            ),
            body = FrameBody.Text(message),
        )
        logSuccess("SEND", "메시지를 보냈습니다. $message")
    }

    private fun sendPing(destination: String = "/pub/ping") {
        runCatching {
            val pingDelay = 30000L
            pingJob = serviceScope.launch {
                while (stompSession != null) {
                    delay(pingDelay)
                    stompSession?.send(
                        headers = StompSendHeaders(destination = destination),
                        body = null,
                    )
                    logSuccess("PING", "Ping sent")
                }
            }
        }.onFailure {
            logError("PING", it)
            serviceScope.launch { disconnect() }
        }
    }

    private suspend fun subscribePong(destination: String = "/topic/pong") {
        runCatching {
            stompSession?.subscribe(
                headers = StompSubscribeHeaders(destination = destination),
            )
        }.onSuccess {
            pongJob = serviceScope.launch {
                it?.collect { logSuccess("PONG", "Pong received") }
            }
        }.onFailure {
            logError("PONG", it)
            disconnect()
        }
    }

    private fun updateStompSocketState(socketState: StompSocketState) {
        stompSocketState = socketState
    }

    private fun logSuccess(header: String, message: String) {
        Timber.tag(TAG).d("✅ $header: $message")
    }

    private fun logError(header: String, error: Throwable) {
        Timber.tag(TAG).e("❌ $header: 오류가 발생했습니다.\n${error.message}")
    }

    private fun handleConnectionFailure() {
        if ((retryCount.incrementAndGet()) <= MAX_RETRY_COUNT) {
            val delay = 2 * 2.0.pow((retryCount.get() - 1).toDouble()).toLong()
            val jitterMillis = (0..delay).random() * 1000
            serviceScope.launch {
                delay(timeMillis = jitterMillis)
                connect(BASE_HOST)
            }
        }
    }

    companion object {
        private const val TAG = "StompSocketManager"
        private const val APPLICATION_JSON = "application/json"
        private const val BASE_HOST = "/"
        private const val MAX_RETRY_COUNT = 5
    }
}
