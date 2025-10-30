package com.napzak.market.chat.datasource

import com.napzak.market.chat.dto.ChatMessageRequest
import com.napzak.market.chat.service.ChatSocketService
import javax.inject.Inject

class ChatSocketDataSource @Inject constructor(
    private val chatSocketService: ChatSocketService,
) {
    fun getMessageFlow() = chatSocketService.messageFlow
    fun getNewChatRequestFlow() = chatSocketService.newChatRequestFlow

    val socketConnectionState = chatSocketService.connectionState

    suspend fun connect() = chatSocketService.connect()

    suspend fun disconnect() = chatSocketService.disconnect()

    suspend fun subscribeChatRoom(roomId: Long) =
        chatSocketService.subscribeChatRoom(roomId)

    suspend fun subscribeCreateChatRoom(storeId: Long) =
        chatSocketService.subscribeCreateChatRoom(storeId)

    suspend fun sendMessage(request: ChatMessageRequest) =
        chatSocketService.sendMessage(request)
}
