package com.napzak.market.product.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductHomeRecommendResponse(
    @SerialName("nickname") val nickname: String,
    @SerialName("productRecommendList") val products: List<ProductResponse>
)