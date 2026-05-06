package com.napzak.market.store.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhoneCodeRequest(
    @SerialName("phoneNumber")
    val phoneNumber: String,
)
