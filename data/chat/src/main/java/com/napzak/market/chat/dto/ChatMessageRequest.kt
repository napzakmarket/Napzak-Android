package com.napzak.market.chat.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageRequest(
    @SerialName("roomId") val roomId: Long,
    @SerialName("type") val type: String,
    @SerialName("content") val content: String?,
    @SerialName("metadata") val metadata: ChatMessageMetadata?,
)
