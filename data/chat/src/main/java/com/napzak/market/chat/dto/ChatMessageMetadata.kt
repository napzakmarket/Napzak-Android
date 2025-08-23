package com.napzak.market.chat.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator


@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
sealed class ChatMessageMetadata {
    @Serializable
    @SerialName("IMAGE")
    data class Image(
        @SerialName("imageUrls") val imageUrls: List<String?>,
    ) : ChatMessageMetadata()

    @Serializable
    @SerialName("PRODUCT")
    data class Product(
        @SerialName("tradeType") val tradeType: String,
        @SerialName("productId") val productId: Long,
        @SerialName("genreName") val genreName: String,
        @SerialName("title") val title: String,
        @SerialName("price") val price: Int,
        @SerialName("isProductDeleted") val isProductDeleted: Int? = null,
    ) : ChatMessageMetadata()

    @Serializable
    @SerialName("EXIT")
    data class EXIT(
        @SerialName("content") val content: String,
    ) : ChatMessageMetadata()

    @Serializable
    @SerialName("REPORTED")
    data class REPORTED(
        @SerialName("content") val content: String,
    ) : ChatMessageMetadata()

    @Serializable
    @SerialName("WITHDRAWN")
    data class WITHDRAWN(
        @SerialName("content") val content: String,
    ) : ChatMessageMetadata()

    @Serializable
    @SerialName("DATE")
    data class Date(
        @SerialName("date") val date: String,
    ) : ChatMessageMetadata()
}