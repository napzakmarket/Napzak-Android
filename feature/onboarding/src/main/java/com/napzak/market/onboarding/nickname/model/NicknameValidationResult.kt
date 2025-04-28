package com.napzak.market.onboarding.nickname.model

sealed interface NicknameValidationResult {
    data object Valid : NicknameValidationResult
    data class Invalid(val errorType: NicknameErrorType) : NicknameValidationResult
    data object Empty : NicknameValidationResult
}