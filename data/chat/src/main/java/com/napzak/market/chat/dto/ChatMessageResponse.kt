package com.napzak.market.chat.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageResponse(
    @SerialName("messageId") val messageId: Long,
    @SerialName("roomId") val roomId: Long,
    @SerialName("senderId") val senderId: Long,
    @SerialName("type") val type: String,
    @SerialName("content") val content: String? = null,
    @SerialName("metadata") val metadata: ChatMessageMetadata? = null,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("isRead") val isRead: Boolean,
    @SerialName("retryCount") val retryCount: Int,
)
