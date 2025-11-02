package com.napzak.market.chat.service

import com.napzak.market.chat.dto.ChatMessageRequest
import com.napzak.market.chat.dto.ChatRealtimeMessage
import com.napzak.market.remote.socket.StompWebSocketClient
import com.napzak.market.remote.socket.type.SocketConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import javax.inject.Inject

interface ChatSocketService {
    val messageFlow: Flow<ChatRealtimeMessage>
    val newChatRequestFlow: Flow<Long>
    val connectionState: Flow<SocketConnectionState>
    suspend fun connect()
    suspend fun disconnect()
    suspend fun subscribeChatRoom(roomId: Long)
    suspend fun sendMessage(request: ChatMessageRequest)
    suspend fun subscribeCreateChatRoom(storeId: Long)
}

class ChatSocketServiceImpl @Inject constructor(
    private val webSocketClient: StompWebSocketClient,
    private val json: Json,
) : ChatSocketService {
    override val messageFlow: Flow<ChatRealtimeMessage> = webSocketClient.messageFlow.mapNotNull {
        runCatching {
            json.decodeFromString(ChatRealtimeMessage.serializer(), it)
        }.getOrNull()
    }

    override val newChatRequestFlow: Flow<Long> = webSocketClient.messageFlow.mapNotNull {
        runCatching {
            json.decodeFromString(Long.serializer(), it)
        }.getOrNull()
    }

    override val connectionState: Flow<SocketConnectionState> = webSocketClient.connectionState

    override suspend fun connect() {
        webSocketClient.connect(BASE_HOST)
    }

    override suspend fun disconnect() {
        webSocketClient.disconnect()
    }

    override suspend fun subscribeChatRoom(roomId: Long) {
        webSocketClient.subscribe(
            destination = Destination.SUBSCRIBE_CHAT_ROOM.format(roomId),
        )
    }

    override suspend fun sendMessage(request: ChatMessageRequest) {
        webSocketClient.send(
            destination = Destination.SEND_CHAT,
            request = request,
            serializer = ChatMessageRequest.serializer(),
        )
    }

    override suspend fun subscribeCreateChatRoom(storeId: Long) {
        webSocketClient.subscribe(
            destination = Destination.SUBS_CREATE_CHAT_ROOMS.format(storeId),
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
