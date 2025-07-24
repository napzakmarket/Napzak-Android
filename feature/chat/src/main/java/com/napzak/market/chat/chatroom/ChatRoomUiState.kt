package com.napzak.market.chat.chatroom

import com.napzak.market.chat.model.ProductBrief
import com.napzak.market.chat.model.StoreBrief

data class ChatRoomUiState(
    val roomId: Long? = null,
    val storeBrief: StoreBrief? = null,
    val productBrief: ProductBrief? = null,
)
