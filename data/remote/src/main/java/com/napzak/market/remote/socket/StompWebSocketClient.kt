package com.napzak.market.remote.socket

import com.napzak.market.remote.socket.type.SocketConnectionState
import com.napzak.market.remote.socket.type.SocketConnectionState.CONNECTED
import com.napzak.market.remote.socket.type.SocketConnectionState.CONNECTING
import com.napzak.market.remote.socket.type.SocketConnectionState.DISCONNECTED
import com.napzak.market.remote.socket.type.SocketMessageType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import kotlin.math.pow

interface StompWebSocketClient {
    val messageFlow: SharedFlow<String>
    val connectionState: Flow<SocketConnectionState>
    suspend fun connect(host: String? = null)
    suspend fun disconnect()
    suspend fun subscribe(destination: String)
    suspend fun <T : Any> send(destination: String, request: T, serializer: KSerializer<T>)
}

class StompWebSocketClientImpl @Inject constructor(
    private val json: Json,
    private val request: Request,
    private val okHttpClient: OkHttpClient,
) : StompWebSocketClient {
    companion object {
        const val TAG = "WebSocketClient"
        private const val BASE_HOST = "/"
        private const val APPLICATION_JSON = "application/json"
        private const val MAX_RETRY_COUNT = 5
    }

    private var coroutineScope: CoroutineScope? = null
    private var webSocket: WebSocket? = null
    private var pingJob: Job? = null
    private val connectionRetryCount = AtomicInteger(0)
    private val connectionMutex = Mutex()

    private val _messageFlow = MutableSharedFlow<String>()
    private val _connectionState = MutableStateFlow<SocketConnectionState>(DISCONNECTED)
    override val messageFlow = _messageFlow.asSharedFlow()
    override val connectionState = _connectionState.asStateFlow()

    override suspend fun connect(host: String?) {
        connectionMutex.withLock {
            coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

            if (_connectionState.value == DISCONNECTED) {
                updateConnectionState(CONNECTING)
                val webSocketListener = StompSocketListener(host)
                webSocket = okHttpClient.newWebSocket(request, webSocketListener)
            }
        }
    }

    override suspend fun disconnect() {
        connectionMutex.withLock {
            runCatching {
                if (_connectionState.value in setOf(CONNECTED, CONNECTING)) {
                    webSocket?.run {
                        val stompFrame = stompFrame { disconnect() }
                        send(stompFrame)
                        close(1000, null)
                    }
                    pingJob?.cancel()
                }
            }.onSuccess {
                updateConnectionState(DISCONNECTED)
                webSocket = null
                coroutineScope?.cancel()
                logSuccess("DISCONNECT", "소켓 연결을 해제했습니다.")
            }.onFailure {
                logError("DISCONNECT", Throwable("소켓 연결을 해제하는데 실패했습니다."))
            }
        }
    }

    override suspend fun subscribe(destination: String) {
        val frame = stompFrame {
            subscribe {
                this.destination = destination
            }
        }
        webSocket?.send(frame)
        logSuccess("SUBSCRIBE", destination)
    }

    override suspend fun <T : Any> send(
        destination: String,
        request: T,
        serializer: KSerializer<T>
    ) {
        runCatching {
            val message = json.encodeToString(serializer, request)
            val frame = stompFrame {
                send {
                    this.destination = destination
                    contentType = APPLICATION_JSON
                    body = message
                }
            }
            webSocket?.send(frame)
        }.onFailure {
            logError("SEND", Throwable("메시지 전송에 실패했습니다 [$destination]"))
        }
    }

    private fun sendPing(destination: String = "/pub/ping") {
        val pingDelay = 25000L
        val pingFrame = stompFrame {
            send {
                this.destination = destination
                body = "[object Object]"
            }
        }

        pingJob?.cancel()
        pingJob = coroutineScope?.launch {
            while (webSocket != null && _connectionState.value == CONNECTED) {
                webSocket?.send(pingFrame)
                logSuccess("HEARTBEAT", "PING!")
                delay(pingDelay)
            }
        }
    }

    private fun subscribePong(destination: String = "/topic/pong") {
        runCatching {
            val pongFrame = stompFrame {
                subscribe {
                    this.destination = destination
                }
            }
            webSocket?.send(pongFrame)
        }.onSuccess {
            logSuccess("HEARTBEAT", "Pong subscribed")
        }
    }

    private fun handleConnectionFailure(host: String) {
        val delay = 2 * 2.0.pow((connectionRetryCount.get() - 1).toDouble()).toLong()
        val jitterMillis = (0..delay).random() * 1000
        CoroutineScope(Dispatchers.Default).launch {
            delay(timeMillis = jitterMillis)
            connect(host)
        }
    }

    private fun updateConnectionState(state: SocketConnectionState) {
        _connectionState.update { state }
    }

    private fun logSuccess(header: String, message: String) {
        Timber.tag(TAG).d("✅ $header: $message")
    }

    private fun logError(header: String, error: Throwable) {
        Timber.tag(TAG).e("❌ $header: 오류가 발생했습니다.\n$error")
    }

    inner class StompSocketListener(host: String? = null) : WebSocketListener() {
        private val stompHost = host ?: BASE_HOST

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            val frame = stompFrame {
                connect {
                    host = stompHost
                }
            }
            webSocket.send(frame)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            if (text.isNotBlank()) {
                val messageType = SocketMessageType.from(text)
                when (messageType) {
                    SocketMessageType.CONNECTED -> {
                        logSuccess("CONNECTED", "소켓이 연결되었습니다.")
                        updateConnectionState(CONNECTED)
                        sendPing()
                        subscribePong()
                        connectionRetryCount.set(0)
                    }

                    SocketMessageType.ERROR -> {
                        logError("MESSAGE", Throwable(text))
                    }

                    SocketMessageType.MESSAGE -> {
                        val body = decodeMessage(text)
                        when {
                            body == "pong" -> logSuccess("HEARTBEAT", "PONG!")

                            else -> {
                                coroutineScope?.launch {
                                    _messageFlow.emit(body)
                                }
                            }
                        }
                    }
                }
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            CoroutineScope(Dispatchers.IO).launch { disconnect() }
            logSuccess("DISCONNECT", "소켓 연결이 해제되었습니다.")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            CoroutineScope(Dispatchers.IO).launch { disconnect() }

            if (connectionRetryCount.incrementAndGet() < MAX_RETRY_COUNT) {
                handleConnectionFailure(stompHost)
            } else {
                logError("CONNECTION", Throwable("소켓 연결에 실패했습니다.", t))
            }
        }
    }
}
