package com.napzak.market.onboarding.phoneVerification.model

import com.napzak.market.store.model.CodeValidationResult
import com.napzak.market.store.model.NameValidationResult
import com.napzak.market.store.model.PhoneValidationResult

data class PhoneVerificationUiState(
    val name: String = "",
    val nameValidation: NameValidationResult = NameValidationResult.Uninitialized,
    val phone: String = "",
    val checkingPhone: String = "",
    val phoneValidation: PhoneValidationResult = PhoneValidationResult.Uninitialized,
    val code: String = "",
    val codeValidation: CodeValidationResult = CodeValidationResult.Uninitialized,
    val isSend: Boolean = false, // 인증하기 / 재전송 문구 flag
    val remainingTimeSec: Int = 0,
    val isAgeChecked: Boolean = false,
    val verificationStatus: VerificationStatus = VerificationStatus.NONE,
    val errorState: PhoneVerificationError = PhoneVerificationError.None, // 모든 에러 토스트 핸들러
) {
    val isSendEnabled: Boolean
        get() = phoneValidation is PhoneValidationResult.Valid
                && remainingTimeSec == 0
                && !isVerificationSuccess
                && errorState != PhoneVerificationError.VerificationRequestLimitExceeded

    val isVerifyEnabled: Boolean
        get() = codeValidation is CodeValidationResult.Valid
                && verificationStatus == VerificationStatus.REQUESTED
                && remainingTimeSec > 0
                && errorState != PhoneVerificationError.VerificationCodeAttemptsExceeded

    val isVerificationSuccess: Boolean
        get() = verificationStatus == VerificationStatus.VERIFIED

    val isNextEnabled: Boolean
        get() = nameValidation is NameValidationResult.Valid
                && isVerificationSuccess
                && isAgeChecked
}
