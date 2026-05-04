package com.napzak.market.store.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhoneCodeResponse(
    @SerialName("remainingRequestCount")
    val remainingRequestCount: Int,
)
