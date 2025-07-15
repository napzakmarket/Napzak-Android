package com.napzak.market.chat.repository

import com.napzak.market.chat.model.ChatItem
import com.napzak.market.chat.model.ChatRoomInformation

interface ChatRoomRepository {
    suspend fun getChatRoomInformation(roomId: Long): Result<ChatRoomInformation>
    suspend fun createChatRoom(productId: Long, receiverId: Long): Result<Long>
    suspend fun getChatRoomMessages(roomId: Long): Result<List<ChatItem<*>>>
    suspend fun enterChatRoom(roomId: Long): Result<Long>
    suspend fun leaveChatRoom(roomId: Long): Result<Unit>
    suspend fun withdrawChatRoom(roomId: Long): Result<Unit>
    suspend fun patchChatRoomProduct(roomId: Long, productId: Long): Result<Long>
}
