package com.napzak.market.store.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WithdrawResponse(
    @SerialName("storeId")
    val storeId: Long,
    @SerialName("withdrawTitle")
    val withdrawTitle: String,
    @SerialName("withdrawDescription")
    val withdrawDescription: String? = null,
)
