package com.napzak.market.chat.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRealtimeMessage(
    @SerialName("messageId") val messageId: Long? = null,
    @SerialName("roomId") val roomId: Long,
    @SerialName("senderId") val senderId: Long? = null,
    @SerialName("type") val type: String,
    @SerialName("content") val content: String? = null,
    @SerialName("metadata") val metadata: ChatMessageMetadata? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("isRead") val isRead: Boolean? = null,
)
