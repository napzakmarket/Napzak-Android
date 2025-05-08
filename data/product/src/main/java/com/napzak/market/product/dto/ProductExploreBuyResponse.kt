package com.napzak.market.product.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductExploreBuyResponse(
    @SerialName("productCount") val productCount: Int?,
    @SerialName("productBuyList") val products: List<ProductResponse>?,
)
