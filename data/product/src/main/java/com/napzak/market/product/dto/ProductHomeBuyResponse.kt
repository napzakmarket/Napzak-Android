package com.napzak.market.product.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductHomeBuyResponse(
    @SerialName("productBuyList") val products: List<ProductResponse>
)