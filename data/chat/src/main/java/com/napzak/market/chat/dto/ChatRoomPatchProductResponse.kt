package com.napzak.market.chat.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomPatchProductResponse(
    @SerialName("updatedProductId")
    val updatedProductId: Long,
)
