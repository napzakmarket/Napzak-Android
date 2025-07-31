package com.napzak.market.chat.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomMessagesResponse(
    @SerialName("messages")
    val messages: List<MessageItem>,
    @SerialName("cursor")
    val cursor: String?,
)

@Serializable
data class MessageItem(
    @SerialName("messageId")
    val messageId: Long?,
    @SerialName("senderId")
    val senderId: Long?,
    @SerialName("type")
    val type: String?,
    @SerialName("content")
    val content: String?,
    @SerialName("metadata")
    val metadata: ChatMessageMetadata?,
    @SerialName("createdAt")
    val createdAt: String?,
    @SerialName("isRead")
    val isRead: Boolean?,
    @SerialName("isMessageOwner")
    val isMessageOwner: Boolean?,
    @SerialName("isProfileNeeded")
    val isProfileNeeded: Boolean?,
)
