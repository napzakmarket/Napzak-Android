package com.napzak.market.chat.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomInformationResponse(
    @SerialName("productInfo") val productInfo: ProductInfo,
    @SerialName("storeInfo") val storeInfo: StoreInfo,
    @SerialName("roomId") val roomId: Long,
) {
    @Serializable
    data class ProductInfo(
        @SerialName("productId") val productId: Long,
        @SerialName("photo") val photo: String,
        @SerialName("tradeType") val tradeType: String,
        @SerialName("title") val title: String,
        @SerialName("price") val price: Int,
        @SerialName("isPriceNegotiable") val isPriceNegotiable: Boolean,
        @SerialName("genreName") val genreName: String,
    )

    @Serializable
    data class StoreInfo(
        @SerialName("storeId") val storeId: Long,
        @SerialName("nickname") val nickname: String,
        @SerialName("isWithdrawn") val isWithdrawn: Boolean,
        @SerialName("storePhoto") val storePhoto: String,
    )
}

