package com.napzak.market.chat.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

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
    val metadata: MessageMetadata?,
    @SerialName("createdAt")
    val createdAt: String?,
    @SerialName("isRead")
    val isRead: Boolean?,
    @SerialName("isMessageOwner")
    val isMessageOwner: Boolean?,
    @SerialName("isProfileNeeded")
    val isProfileNeeded: Boolean?,
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
sealed class MessageMetadata {
    @Serializable
    @SerialName("IMAGE")
    data class Image(
        @SerialName("imageUrls") val imageUrls: List<String>,
    ) : MessageMetadata()

    @Serializable
    @SerialName("PRODUCT")
    data class Product(
        @SerialName("tradeType") val tradeType: String,
        @SerialName("productId") val productId: Long,
        @SerialName("genreName") val genreName: String,
        @SerialName("title") val title: String,
        @SerialName("price") val price: Int,
    ) : MessageMetadata()

    @Serializable
    @SerialName("SYSTEM")
    data class System(
        @SerialName("content") val content: String,
    ) : MessageMetadata()

    @Serializable
    @SerialName("DATE")
    data class Date(
        @SerialName("date") val content: String,
    ) : MessageMetadata()
}
