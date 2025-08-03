package com.napzak.market.chat.repository

import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.model.SendMessage
import kotlinx.coroutines.flow.Flow

interface ChatSocketRepository {
    val errorFlow: Flow<Throwable>
    suspend fun connect(): Result<Unit>
    suspend fun disconnect(): Result<Unit>
    suspend fun subscribeChatRoom(roomId: Long, storeId: Long): Result<Flow<ReceiveMessage<*>>>
    suspend fun subscribeCreateChatRoom(storeId: Long): Result<Flow<Long>>
    suspend fun sendChat(chat: SendMessage<*>): Result<Unit>
}