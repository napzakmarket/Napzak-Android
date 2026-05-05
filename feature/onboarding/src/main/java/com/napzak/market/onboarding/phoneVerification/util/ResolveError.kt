package com.napzak.market.onboarding.phoneVerification.util

import com.napzak.market.onboarding.phoneVerification.model.PhoneVerificationError
import com.napzak.market.onboarding.phoneVerification.model.PhoneVerificationUiState
import com.napzak.market.onboarding.phoneVerification.model.VerificationStatus
import com.napzak.market.store.model.CodeValidationResult
import com.napzak.market.store.model.NameValidationResult
import com.napzak.market.store.model.PhoneValidationResult

fun resolveError(state: PhoneVerificationUiState): PhoneVerificationError {
    return when {
        state.verificationStatus == VerificationStatus.REQUESTED
                && state.remainingTimeSec <= 0
                    -> PhoneVerificationError.VerificationTimeExpired

        state.verificationStatus == VerificationStatus.REQUESTED
                && state.codeValidation is CodeValidationResult.Invalid
                    -> PhoneVerificationError.InvalidVerificationCode

        state.nameValidation is NameValidationResult.Invalid ->
            PhoneVerificationError.InvalidName

        state.phoneValidation is PhoneValidationResult.Invalid ->
            PhoneVerificationError.InvalidPhoneNumber

        else -> PhoneVerificationError.None
    }
}
