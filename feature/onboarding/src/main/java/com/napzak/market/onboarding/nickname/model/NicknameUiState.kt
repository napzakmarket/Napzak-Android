package com.napzak.market.onboarding.nickname.model

data class NicknameUiState(
    val nickname: String = "",
    val validationResult: NicknameValidationResult = NicknameValidationResult.Empty,
) {
    val isNextEnabled: Boolean
        get() = validationResult is NicknameValidationResult.Valid
}
