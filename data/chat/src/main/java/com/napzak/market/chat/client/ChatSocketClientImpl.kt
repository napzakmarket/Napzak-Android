package com.napzak.market.chat.client

import com.napzak.market.chat.dto.ChatMessageRequest
import com.napzak.market.chat.dto.ChatMessageResponse
import com.napzak.market.remote.StompSocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ChatSocketClientImpl @Inject constructor(
    private val stompSocketManager: StompSocketManager,
) : ChatSocketClient {
    private val clientScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val flowMap = mutableMapOf<Long, Flow<ChatMessageResponse>>()
    private val jobMap = mutableMapOf<Long, Job>()

    private val _errorFlow = MutableSharedFlow<Exception>()
    private val _messageFlow = MutableSharedFlow<ChatMessageResponse>()

    override val errorFlow = _errorFlow.asSharedFlow()
    override val messageFlow = _messageFlow.asSharedFlow()

    override suspend fun connect() {
        this.stompSocketManager.connect()
    }

    override suspend fun disconnect() {
        jobMap.values.forEach { it.cancel() }
        jobMap.clear()
        flowMap.clear()

        this.stompSocketManager.disconnect()
    }

    override suspend fun subscribeChatRoom(roomId: Long) {
        return try {
            if (jobMap.containsKey(roomId)) return

            val flow = stompSocketManager.subscribe(
                destination = DESTINATION_SUBSCRIBE_CHAT_ROOM.format(roomId),
                deserializer = ChatMessageResponse.serializer()
            )

            val job = clientScope.launch {
                flow.collect {
                    logSuccess(
                        "SUBSCRIBE",
                        "메시지 도착: ${it.roomId} - ${it.content}, ${it.metadata.toString()}"
                    )
                    _messageFlow.emit(it)
                }
            }

            logSuccess("SUBSCRIBE", "소켓 채널이 구독되었습니다. $roomId")
            jobMap[roomId] = job
            flowMap[roomId] = flow
        } catch (e: Exception) {
            logError("SUBSCRIBE", e)
            stompSocketManager.disconnect()
            throw e
        }
    }

    override suspend fun sendMessage(request: ChatMessageRequest) {
        try {
            stompSocketManager.send(
                destination = DESTINATION_SEND_CHAT,
                request = request,
                serializer = ChatMessageRequest.serializer(),
            )
        } catch (e: Exception) {
            logError("SEND", e)
            disconnect()
        }
    }

    private fun logSuccess(header: String, message: String) {
        Timber.tag(TAG).d("✅ $header: $message")
    }

    private fun logError(header: String, error: Throwable) {
        Timber.tag(TAG).e("❌ $header: 오류가 발생했습니다.\n${error.message}")
    }

    companion object {
        private const val TAG = "ChatSocketClient"
        private const val DESTINATION_SEND_CHAT = "/pub/chat/send"
        private const val DESTINATION_SUBSCRIBE_CHAT_ROOM = "/topic/chat.room.%s"
    }
}
