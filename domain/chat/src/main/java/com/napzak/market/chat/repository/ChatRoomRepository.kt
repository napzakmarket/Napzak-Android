package com.napzak.market.chat.repository

import com.napzak.market.chat.model.ChatRoomInformation
import com.napzak.market.chat.model.ReceiveMessage

interface ChatRoomRepository {
    suspend fun getChatRoomInformation(productId: Long): Result<ChatRoomInformation>
    suspend fun createChatRoom(productId: Long, receiverId: Long): Result<Long>
    suspend fun getChatRoomMessages(roomId: Long): Result<List<ReceiveMessage<*>>>
    suspend fun enterChatRoom(roomId: Long): Result<Long>
    suspend fun leaveChatRoom(roomId: Long): Result<Unit>
    suspend fun withdrawChatRoom(roomId: Long): Result<Unit>
    suspend fun patchChatRoomProduct(roomId: Long, productId: Long): Result<Long>
}
