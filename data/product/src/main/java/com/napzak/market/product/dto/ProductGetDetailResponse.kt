package com.napzak.market.product.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductGetDetailResponse(
    @SerialName("isInterested") val isInterested: Boolean,
    @SerialName("productDetail") val productDetail: ProductDetailResponse,
    @SerialName("productPhotoList") val productPhotoList: List<ProductPhotoResponse>,
    @SerialName("storeInfo") val storeInfo: StoreInfoResponse,
) {
    @Serializable
    data class ProductDetailResponse(
        @SerialName("productId") val productId: Long,
        @SerialName("tradeType") val tradeType: String,
        @SerialName("genreName") val genreName: String,
        @SerialName("productName") val productName: String,
        @SerialName("price") val price: Int,
        @SerialName("uploadTime") val uploadTime: String,
        @SerialName("chatCount") val chatCount: Int,
        @SerialName("interestCount") val interestCount: Int,
        @SerialName("description") val description: String,
        @SerialName("productCondition") val productCondition: String,
        @SerialName("standardDeliveryFee") val standardDeliveryFee: Int,
        @SerialName("halfDeliveryFee") val halfDeliveryFee: Int,
        @SerialName("isDeliveryIncluded") val isDeliveryIncluded: Boolean,
        @SerialName("isPriceNegotiable") val isPriceNegotiable: Boolean,
        @SerialName("tradeStatus") val tradeStatus: String,
        @SerialName("isOwnedByCurrentUser") val isOwnedByCurrentUser: Boolean
    )

    @Serializable
    data class ProductPhotoResponse(
        @SerialName("photoId") val photoId: Long,
        @SerialName("photoUrl") val photoUrl: String,
        @SerialName("photoSequence") val photoSequence: Int
    )

    @Serializable
    data class StoreInfoResponse(
        @SerialName("userId") val userId: Long,
        @SerialName("storePhoto") val storePhoto: String,
        @SerialName("nickname") val nickname: String,
        @SerialName("totalSellCount") val totalSellCount: Int,
        @SerialName("totalBuyCount") val totalBuyCount: Int
    )
}