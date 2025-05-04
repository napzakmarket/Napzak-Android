package com.napzak.market.product.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductExploreSellResponse(
    @SerialName("productCount") val productCount: Int?,
    @SerialName("productSellList") val products: List<ProductResponse>?,
)
