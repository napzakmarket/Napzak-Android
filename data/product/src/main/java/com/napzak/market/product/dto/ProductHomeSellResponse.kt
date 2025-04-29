package com.napzak.market.product.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductHomeSellResponse(
    @SerialName("productSellList") val products: List<ProductResponse>,
)
