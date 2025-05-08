package com.napzak.market.product.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductPatchTradeStatusRequest(
    @SerialName("tradeStatus")
    val tradeStatus: String,
)
