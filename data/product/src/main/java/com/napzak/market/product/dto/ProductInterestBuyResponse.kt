package com.napzak.market.product.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductInterestBuyResponse(
    @SerialName("interestedBuyProductList") val products: List<ProductResponse>,
    @SerialName("nextCursor") val nextCursor: String? = null,
)
