package com.napzak.market.chat.service

import com.napzak.market.chat.dto.ChatMessageRequest
import com.napzak.market.chat.dto.ChatMessageResponse
import com.napzak.market.remote.StompSocketClient
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.builtins.serializer
import javax.inject.Inject

class ChatSocketService @Inject constructor(
    private val stompSocketClient: StompSocketClient,
) {
    suspend fun connect() {
        this.stompSocketClient.connect(BASE_HOST)
    }

    suspend fun disconnect() {
        this.stompSocketClient.disconnect()
    }

    suspend fun subscribeChatRoom(roomId: Long): Flow<ChatMessageResponse> {
        return stompSocketClient.subscribe(
            destination = DESTINATION_SUBSCRIBE_CHAT_ROOM.format(roomId),
            deserializer = ChatMessageResponse.serializer(),
        )
    }

    suspend fun sendMessage(request: ChatMessageRequest) {
        stompSocketClient.send(
            destination = DESTINATION_SEND_CHAT,
            request = request,
            serializer = ChatMessageRequest.serializer(),
        )
    }

    suspend fun subscribeCreateChatRoom(storeId: Long): Flow<Long> {
        return stompSocketClient.subscribe(
            destination = DESTINATION_SUBS_CREATE_CHAT_ROOMS.format(storeId),
            deserializer = Long.serializer()
        )
    }

    companion object {
        private const val BASE_HOST = "/"
        private const val DESTINATION_SEND_CHAT = "/pub/chat/send"
        private const val DESTINATION_SUBSCRIBE_CHAT_ROOM = "/topic/chat.room.%s"
        private const val DESTINATION_SUBS_CREATE_CHAT_ROOMS = "/queue/chat.room-created.%s"
    }
}
