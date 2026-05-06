package com.napzak.market.store.usecase

import com.napzak.market.store.model.PhoneValidationResult
import javax.inject.Inject

class ValidatePhoneUseCase @Inject constructor() {

    operator fun invoke(input: String): PhoneValidationResult {
        val phone = input.filter { it.isDigit() }
        validate(input = phone)?.let { return it }

        return PhoneValidationResult.Valid
    }

    private fun validate(input: String): PhoneValidationResult.Invalid? {
        return when {
            input.isEmpty() ->
                PhoneValidationResult.Invalid(PhoneValidationResult.Error.EMPTY)

            !input.matches(ONLY_NUMBER_REGEX) ->
                PhoneValidationResult.Invalid(PhoneValidationResult.Error.INVALID_FORMAT)

            input.length !in MIN_LENGTH..MAX_LENGTH ->
                PhoneValidationResult.Invalid(PhoneValidationResult.Error.INVALID_LENGTH)

            else -> null
        }
    }

    companion object {
        private val ONLY_NUMBER_REGEX = Regex("^[0-9]+$")
        private const val MIN_LENGTH = 10
        const val MAX_LENGTH = 11
    }
}