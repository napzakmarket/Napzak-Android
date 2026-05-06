package com.napzak.market.store.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CodeVerificationRequest(
    @SerialName("phoneNumber")
    val phoneNumber: String,
    @SerialName("code")
    val code: String,
)
