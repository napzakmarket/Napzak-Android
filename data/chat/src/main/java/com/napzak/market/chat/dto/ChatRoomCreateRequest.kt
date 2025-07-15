package com.napzak.market.chat.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomCreateRequest(
    @SerialName("productId")
    val productId: Long,
    @SerialName("receiverId")
    val receiverId: Long,
)
