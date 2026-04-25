package com.napzak.market.store.usecase

import com.napzak.market.store.model.NameValidationResult
import javax.inject.Inject

class ValidateNameUseCase @Inject constructor() {
    operator fun invoke(input: String): NameValidationResult {
        val name = input.trim()

        if (name.isEmpty()) {
            return NameValidationResult.Invalid(NameValidationResult.Error.EMPTY)
        }

        if (name.length < MIN_LENGTH) {
            return NameValidationResult.Invalid(NameValidationResult.Error.TOO_SHORT)
        }

        if (name.length > MAX_LENGTH) {
            return NameValidationResult.Invalid(NameValidationResult.Error.TOO_LONG)
        }

        if (!name.matches(VALID_NAME_REGEX)) {
            return NameValidationResult.Invalid(NameValidationResult.Error.INVALID_FORMAT)
        }

        return NameValidationResult.Valid
    }

    companion object {
        private const val MIN_LENGTH = 2
        private const val MAX_LENGTH = 20
        private val VALID_NAME_REGEX = Regex("^[가-힣]+$")
    }
}
