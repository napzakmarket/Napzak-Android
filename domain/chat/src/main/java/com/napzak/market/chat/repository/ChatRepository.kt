package com.napzak.market.chat.repository

import com.napzak.market.chat.model.ChatRoom

interface ChatRepository {
    suspend fun getChatRoomIds(): Result<List<Long>>
    suspend fun getChatRooms(): Result<Pair<Long, List<ChatRoom>>>
}
