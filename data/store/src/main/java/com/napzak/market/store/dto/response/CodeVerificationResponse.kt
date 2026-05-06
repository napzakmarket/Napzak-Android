package com.napzak.market.store.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CodeVerificationResponse(
    @SerialName("isPhoneVerified")
    val isPhoneVerified: Boolean,
    @SerialName("remainingRequestCount")
    val remainingRequestCount: Int,
)
