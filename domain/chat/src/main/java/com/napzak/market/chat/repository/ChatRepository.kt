package com.napzak.market.chat.repository

import com.napzak.market.chat.model.ChatRoom
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChatRoomIds(): Result<List<Long>>

    suspend fun fetchChatRoomsFromRemote(): Result<Unit>

    fun getChatRoomsFlow(): Flow<List<ChatRoom>>
}
