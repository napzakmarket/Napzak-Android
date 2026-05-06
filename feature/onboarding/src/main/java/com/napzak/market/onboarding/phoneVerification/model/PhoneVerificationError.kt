package com.napzak.market.onboarding.phoneVerification.model

sealed class PhoneVerificationError {
    object None : PhoneVerificationError()
    object InvalidName : PhoneVerificationError()
    object InvalidPhoneNumber : PhoneVerificationError()
    object PhoneAlreadyRegistered : PhoneVerificationError()
    object PhoneNotAllowed : PhoneVerificationError()
    object NetworkError : PhoneVerificationError()
    object VerificationRequestLimitExceeded : PhoneVerificationError()
    object VerificationTimeExpired : PhoneVerificationError()
    object InvalidVerificationCode : PhoneVerificationError()
    object VerificationCodeAttemptsExceeded : PhoneVerificationError()
}
