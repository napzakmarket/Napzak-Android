package com.napzak.market.chat.repository

import com.napzak.market.chat.model.Chat
import com.napzak.market.chat.model.ChatItem
import kotlinx.coroutines.flow.Flow

interface ChatSocketRepository {
    val messageFlow: Flow<ChatItem<*>>
    val errorFlow: Flow<Exception>
    suspend fun connect(): Result<Unit>
    suspend fun disconnect(): Result<Unit>
    suspend fun subscribeChatRoom(roomId: Long): Result<Unit>
    suspend fun sendChat(chat: Chat<*>): Result<Unit>
}