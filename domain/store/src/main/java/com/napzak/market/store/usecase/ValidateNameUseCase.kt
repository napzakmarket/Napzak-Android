package com.napzak.market.store.usecase

import com.napzak.market.store.model.NameValidationResult
import javax.inject.Inject

class ValidateNameUseCase @Inject constructor() {
    operator fun invoke(input: String): NameValidationResult {
        val name = input.trim()
        validate(name)?.let { return it }

        return NameValidationResult.Valid
    }

    private fun validate(input: String): NameValidationResult.Invalid? {
        return when {
            input.isEmpty() ->
                NameValidationResult.Invalid(NameValidationResult.Error.EMPTY)

            input.length < MIN_LENGTH ->
                NameValidationResult.Invalid(NameValidationResult.Error.TOO_SHORT)

            input.length > MAX_LENGTH ->
                NameValidationResult.Invalid(NameValidationResult.Error.TOO_LONG)

            !input.matches(VALID_NAME_REGEX) ->
                NameValidationResult.Invalid(NameValidationResult.Error.INVALID_FORMAT)

            else -> null
        }
    }

    companion object {
        private val VALID_NAME_REGEX = Regex("^[가-힣]+$")
        private const val MIN_LENGTH = 2
        const val MAX_LENGTH = 20
    }
}
