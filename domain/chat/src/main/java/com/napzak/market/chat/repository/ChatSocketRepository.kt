package com.napzak.market.chat.repository

import com.napzak.market.chat.model.SendMessage
import kotlinx.coroutines.CoroutineScope

interface ChatSocketRepository {
    suspend fun connect(): Result<Unit>
    suspend fun disconnect(): Result<Unit>
    suspend fun subscribeChatRoom(roomId: Long): Result<Unit>
    suspend fun subscribeCreateChatRoom(storeId: Long): Result<Unit>
    suspend fun sendChat(chat: SendMessage<*>): Result<Unit>
    fun collectMessagesFromSocket(storeId: Long, coroutineScope: CoroutineScope)
    fun collectNewChatRequestFromSocket(coroutineScope: CoroutineScope)
}