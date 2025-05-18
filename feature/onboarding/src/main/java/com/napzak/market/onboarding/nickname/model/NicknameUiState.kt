package com.napzak.market.onboarding.nickname.model

import com.napzak.market.store.model.NicknameValidationResult

data class NicknameUiState(
    val nickname: String = "",
    val validationResult: NicknameValidationResult = NicknameValidationResult.Uninitialized,
    val isChecking: Boolean = false,
    val isAvailable: Boolean? = null,
    val duplicationError: String? = null,
) {
    val isNextEnabled: Boolean
        get() = validationResult is NicknameValidationResult.Valid && duplicationError == null
}