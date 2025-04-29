package com.napzak.market.product.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    @SerialName("productId") val productId: Long?,
    @SerialName("genreName") val genreName: String?,
    @SerialName("productName") val productName: String?,
    @SerialName("photo") val photo: String?,
    @SerialName("price") val price: Int?,
    @SerialName("uploadTime") val uploadTime: String?,
    @SerialName("isInterested") val isInterested: Boolean?,
    @SerialName("tradeType") val tradeType: String?,
    @SerialName("tradeStatus") val tradeStatus: String?,
    @SerialName("isPriceNegotiable") val isPriceNegotiable: Boolean? = null, // 구해요 상품에만 존재한다.
    @SerialName("isOwnedByCurrentUser") val isOwnedByCurrentUser: Boolean?,
    @SerialName("interestCount") val interestCount: Int?,
    @SerialName("chatCount") val chatCount: Int?,
)