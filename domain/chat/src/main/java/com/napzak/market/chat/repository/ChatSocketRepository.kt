package com.napzak.market.chat.repository

import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.model.SendMessage
import kotlinx.coroutines.flow.Flow

interface ChatSocketRepository {
    suspend fun connect(): Result<Unit>
    suspend fun disconnect(): Result<Unit>
    suspend fun subscribeChatRoom(roomId: Long): Result<Unit>
    suspend fun subscribeCreateChatRoom(storeId: Long): Result<Unit>
    suspend fun sendChat(chat: SendMessage<*>): Result<Unit>
    fun getChatMessageStream(storeId: Long): Flow<ReceiveMessage<*>>
    fun getNewChatRequestStream(): Flow<Long>
}