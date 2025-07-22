package com.napzak.market.chat.datasource

import com.napzak.market.chat.client.ChatSocketClient
import com.napzak.market.chat.dto.ChatMessageRequest
import com.napzak.market.chat.dto.ChatMessageResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatSocketDataSource @Inject constructor(
    private val chatSocketClient: ChatSocketClient,
) {
    val messageFlow: Flow<ChatMessageResponse> = chatSocketClient.messageFlow
    val errorFlow: Flow<Exception> = chatSocketClient.errorFlow

    suspend fun connect() = chatSocketClient.connect()

    suspend fun disconnect() = chatSocketClient.disconnect()

    suspend fun subscribeChatRoom(roomId: Long) =
        chatSocketClient.subscribeChatRoom(roomId)

    suspend fun sendMessage(request: ChatMessageRequest) =
        chatSocketClient.sendMessage(request)
}
