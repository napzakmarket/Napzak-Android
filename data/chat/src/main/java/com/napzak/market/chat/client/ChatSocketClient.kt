package com.napzak.market.chat.client

import com.napzak.market.chat.dto.ChatMessageRequest
import com.napzak.market.chat.dto.ChatMessageResponse
import kotlinx.coroutines.flow.Flow

interface ChatSocketClient {
    val messageFlow: Flow<ChatMessageResponse>
    val errorFlow: Flow<Exception>
    suspend fun connect()
    suspend fun disconnect()
    suspend fun subscribeChatRoom(roomId: Long)
    suspend fun sendMessage(request: ChatMessageRequest)
}