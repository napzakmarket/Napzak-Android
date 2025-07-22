package com.napzak.market.chat.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomEnterResponse(
    @SerialName("productId")
    val productId: Long,
)
