package com.napzak.market.chat.client

import com.napzak.market.chat.dto.ChatMessageRequest
import com.napzak.market.chat.dto.ChatMessageResponse
import com.napzak.market.remote.StompSocketClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.serialization.builtins.serializer
import javax.inject.Inject

class ChatSocketClientImpl @Inject constructor(
    private val stompSocketClient: StompSocketClient,
) : ChatSocketClient {
    private val _errorFlow = MutableSharedFlow<Throwable>()
    override val errorFlow = _errorFlow.asSharedFlow()

    override suspend fun connect() {
        this.stompSocketClient.connect(BASE_HOST)
    }

    override suspend fun disconnect() {
        this.stompSocketClient.disconnect()
    }

    override suspend fun subscribeChatRoom(roomId: Long): Flow<ChatMessageResponse> {
        return stompSocketClient.subscribe(
            destination = DESTINATION_SUBSCRIBE_CHAT_ROOM.format(roomId),
            deserializer = ChatMessageResponse.serializer(),
        ).catch { _errorFlow.emit(it) }
    }

    override suspend fun sendMessage(request: ChatMessageRequest) {
        try {
            stompSocketClient.send(
                destination = DESTINATION_SEND_CHAT,
                request = request,
                serializer = ChatMessageRequest.serializer(),
            )
        } catch (e: Exception) {
            _errorFlow.emit(e)
        }
    }

    override suspend fun subscribeCreateChatRoom(storeId: Long): Flow<Long> {
        return stompSocketClient.subscribe(
            destination = DESTINATION_SUBS_CREATE_CHAT_ROOMS.format(storeId),
            deserializer = Long.serializer()
        ).catch { _errorFlow.emit(it) }
    }

    companion object {
        private const val BASE_HOST = "/"
        private const val DESTINATION_SEND_CHAT = "/pub/chat/send"
        private const val DESTINATION_SUBSCRIBE_CHAT_ROOM = "/topic/chat.room.%s"
        private const val DESTINATION_SUBS_CREATE_CHAT_ROOMS = "/queue/chat.room-created.%s"
    }
}
