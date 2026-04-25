package com.napzak.market.store.usecase

import com.napzak.market.store.model.PhoneValidationResult
import javax.inject.Inject

class ValidatePhoneUseCase @Inject constructor() {

    operator fun invoke(input: String): PhoneValidationResult {

        if (input.isEmpty()) {
            return PhoneValidationResult.Invalid(PhoneValidationResult.Error.EMPTY)
        }

        if (!input.matches(ONLY_NUMBER_REGEX)) {
            return PhoneValidationResult.Invalid(PhoneValidationResult.Error.INVALID_FORMAT)
        }

        if (input.length !in MIN_LENGTH..MAX_LENGTH) {
            return PhoneValidationResult.Invalid(PhoneValidationResult.Error.INVALID_LENGTH)
        }

        return PhoneValidationResult.Valid
    }

    companion object {
        private const val MIN_LENGTH = 10
        private const val MAX_LENGTH = 11
        private val ONLY_NUMBER_REGEX = Regex("^[0-9]+$")
    }
}