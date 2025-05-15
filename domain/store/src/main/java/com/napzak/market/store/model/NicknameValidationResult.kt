package com.napzak.market.store.model

sealed interface NicknameValidationResult {
    data class Valid(val message: String = "") : NicknameValidationResult
    data class Invalid(val errorMessage: String) : NicknameValidationResult
    data object Empty : NicknameValidationResult
}

