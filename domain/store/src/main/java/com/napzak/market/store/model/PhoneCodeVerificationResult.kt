package com.napzak.market.store.model

data class PhoneCodeVerificationResult(
    val isPhoneVerified: Boolean,
    val remainingRequestCount: Int,
)
