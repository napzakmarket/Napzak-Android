package com.napzak.market.chat.datasource

import com.napzak.market.chat.client.ChatSocketClient
import com.napzak.market.chat.dto.ChatMessageRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatSocketDataSource @Inject constructor(
    private val chatSocketClient: ChatSocketClient,
) {
    val errorFlow: Flow<Throwable> = chatSocketClient.errorFlow

    suspend fun connect() = chatSocketClient.connect()

    suspend fun disconnect() = chatSocketClient.disconnect()

    suspend fun subscribeChatRoom(roomId: Long) =
        chatSocketClient.subscribeChatRoom(roomId)

    suspend fun subscribeCreateChatRoom(storeId: Long) =
        chatSocketClient.subscribeCreateChatRoom(storeId)

    suspend fun sendMessage(request: ChatMessageRequest) =
        chatSocketClient.sendMessage(request)
}
