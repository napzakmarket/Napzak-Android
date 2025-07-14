package com.napzak.market.product.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductInterestSellResponse(
    @SerialName("interestedSellProductList") val products: List<ProductResponse>,
    @SerialName("nextCursor") val nextCursor: String?,
)
