package com.napzak.market.onboarding.phoneVerification

sealed interface PhoneVerificationSideEffect {
    data object OnCodeSend: PhoneVerificationSideEffect
    data object OnUserVerify: PhoneVerificationSideEffect
}
