package com.napzak.market.chat.repository

import com.napzak.market.chat.model.ProductBrief
import com.napzak.market.chat.model.StoreBrief

interface ChatRoomRepository {
    suspend fun getChatRoomInformation(roomId: Long): Result<Pair<Long?, Pair<ProductBrief, StoreBrief>>>
    suspend fun createChatRoom(productId: Long, receiverId: Long): Result<Long>
    suspend fun enterChatRoom(roomId: Long): Result<Unit>
    suspend fun leaveChatRoom(roomId: Long): Result<Unit>
    suspend fun withdrawChatRoom(roomId: Long): Result<Unit>
    suspend fun patchChatRoomProduct(roomId: Long, productId: Long): Result<Long>
}