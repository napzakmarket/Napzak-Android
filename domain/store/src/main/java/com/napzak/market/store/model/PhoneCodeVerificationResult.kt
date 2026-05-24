package com.napzak.market.store.model

data class PhoneCodeVerificationResult(
    val isCodeVerified: Boolean,
    val remainingRequestCount: Int,
)
