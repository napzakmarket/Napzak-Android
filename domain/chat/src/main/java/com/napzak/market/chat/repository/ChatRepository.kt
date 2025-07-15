package com.napzak.market.chat.repository

import com.napzak.market.chat.model.ChatRoom

interface ChatRepository {
    suspend fun getChatRooms(): Result<List<ChatRoom>>
}