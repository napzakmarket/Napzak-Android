package com.napzak.market.store.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WithdrawRequest(
    @SerialName("withdrawTitle")
    val withdrawTitle: String,
    @SerialName("withdrawDescription")
    val withdrawDescription: String? = null,
)