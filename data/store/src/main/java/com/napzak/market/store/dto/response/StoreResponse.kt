package com.napzak.market.store.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoreResponse(
    @SerialName("storeId") val storeId: Long,
    @SerialName("storeNickName") val storeNickName: String,
    @SerialName("storePhoto") val storePhoto: String,
    @SerialName("totalSellCount") val totalSellCount: Int,
    @SerialName("totalBuyCount") val totalBuyCount: Int,
    @SerialName("serviceLink") val serviceLink: String,
)