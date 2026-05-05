package com.napzak.market.store.usecase

import com.napzak.market.store.model.CodeValidationResult
import javax.inject.Inject

class ValidateCodeUseCase @Inject constructor() {

    operator fun invoke(input: String): CodeValidationResult {
        validate(input)?.let { return it }

        return CodeValidationResult.Valid
    }

    private fun validate(input: String): CodeValidationResult.Invalid? {
        return when {
            input.isEmpty() ->
                CodeValidationResult.Invalid(CodeValidationResult.Error.EMPTY)

            !input.matches(CODE_REGEX) ->
                CodeValidationResult.Invalid(CodeValidationResult.Error.INVALID_FORMAT)

            else -> null
        }
    }

    companion object {
        private val CODE_REGEX = Regex("^[0-9]{6}$")
        const val MAX_LENGTH = 6
    }
}
