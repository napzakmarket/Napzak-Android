package com.napzak.market.onboarding.phoneVerification.model

import com.napzak.market.store.model.CodeValidationResult
import com.napzak.market.store.model.NameValidationResult
import com.napzak.market.store.model.PhoneValidationResult

data class PhoneVerificationUiState(
    val name: String = "",
    val nameValidation: NameValidationResult = NameValidationResult.Uninitialized,
    val phone: String = "",
    val phoneValidation: PhoneValidationResult = PhoneValidationResult.Uninitialized,
    val code: String = "",
    val codeValidation: CodeValidationResult = CodeValidationResult.Uninitialized,
    val isSend: Boolean = false,
    val remainingTimeSec: Int = 0,
    val verificationStatus: VerificationStatus = VerificationStatus.NONE,
    val isVerificationSuccess: Boolean = false,
    val isAgeChecked: Boolean = false,
) {
    val isSendEnabled: Boolean
        get() =
            nameValidation is NameValidationResult.Valid &&
                    phoneValidation is PhoneValidationResult.Valid &&
                    verificationStatus == VerificationStatus.NONE

    val isVerifyEnabled: Boolean
        get() =
            codeValidation is CodeValidationResult.Valid &&
                    verificationStatus == VerificationStatus.REQUESTED &&
                    remainingTimeSec > 0

    val isNextEnabled: Boolean
        get() = verificationStatus == VerificationStatus.VERIFIED
}
