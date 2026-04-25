package com.napzak.market.store.usecase

import com.napzak.market.store.model.CodeValidationResult
import javax.inject.Inject

class ValidateCodeUseCase @Inject constructor() {

    operator fun invoke(input: String): CodeValidationResult {

        if (input.isEmpty()) {
            return CodeValidationResult.Invalid(CodeValidationResult.Error.EMPTY)
        }

        if (!input.matches(CODE_REGEX)) {
            return CodeValidationResult.Invalid(CodeValidationResult.Error.INVALID_FORMAT)
        }

        return CodeValidationResult.Valid
    }

    companion object {
        private val CODE_REGEX = Regex("^[0-9]{6}$")
    }
}
