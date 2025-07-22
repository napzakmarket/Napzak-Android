package com.napzak.market.chat.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomIdsResponse(
    @SerialName("chatRoomIds") val chatRoomIds: List<Long>,
)