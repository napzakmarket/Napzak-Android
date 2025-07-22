package com.napzak.market.chat.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomCreateResponse(
    @SerialName("roomId")
    val roomId: Long,
)
