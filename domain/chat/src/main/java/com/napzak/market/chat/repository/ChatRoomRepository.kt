package com.napzak.market.chat.repository

import com.napzak.market.chat.model.ProductBrief
import com.napzak.market.chat.model.StoreBrief

interface ChatRoomRepository {
    suspend fun getChatRoomInformation(roomId: Long): Result<Pair<Long?, Pair<ProductBrief, StoreBrief>>>
}