package com.napzak.market.chat.service

import com.napzak.market.chat.dto.ChatMessageRequest
import com.napzak.market.chat.dto.ChatRealtimeMessage
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

    suspend fun subscribeChatRoom(roomId: Long): Flow<ChatRealtimeMessage> {
        return stompSocketClient.subscribe(
            destination = Destination.SUBSCRIBE_CHAT_ROOM.format(roomId),
            deserializer = ChatRealtimeMessage.serializer(),
        )
    }

    suspend fun sendMessage(request: ChatMessageRequest) {
        stompSocketClient.send(
            destination = Destination.SEND_CHAT,
            request = request,
            serializer = ChatMessageRequest.serializer(),
        )
    }

    suspend fun subscribeCreateChatRoom(storeId: Long): Flow<Long> {
        return stompSocketClient.subscribe(
            destination = Destination.SUBS_CREATE_CHAT_ROOMS.format(storeId),
            deserializer = Long.serializer()
        )
    }

    private object Destination {
        const val SEND_CHAT = "/pub/chat/send"
        const val SUBSCRIBE_CHAT_ROOM = "/topic/chat.room.%s"
        const val SUBS_CREATE_CHAT_ROOMS = "/queue/chat.room-created.%s"
    }

    companion object {
        private const val BASE_HOST = "/"
    }
}
