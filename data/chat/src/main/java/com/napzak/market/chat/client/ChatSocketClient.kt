package com.napzak.market.chat.client

import com.napzak.market.chat.dto.ChatMessageRequest
import com.napzak.market.chat.dto.ChatMessageResponse
import kotlinx.coroutines.flow.Flow

interface ChatSocketClient {
    val errorFlow: Flow<Throwable>
    suspend fun connect()
    suspend fun disconnect()
    suspend fun subscribeChatRoom(roomId: Long): Flow<ChatMessageResponse>
    suspend fun subscribeCreateChatRoom(storeId: Long): Flow<Long>
    suspend fun sendMessage(request: ChatMessageRequest)
}