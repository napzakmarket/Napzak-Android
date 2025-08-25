package com.napzak.market.store.usecase

import com.napzak.market.store.model.NicknameValidationResult
import javax.inject.Inject

class ValidateNicknameUseCase @Inject constructor() {

    operator fun invoke(input: String): NicknameValidationResult {
        if (input.isBlank() || input.length < MIN_LENGTH) {
            return NicknameValidationResult.Invalid(NicknameValidationResult.Error.EMPTY)
        }

        if (input.length > MAX_LENGTH) {
            return NicknameValidationResult.Invalid(NicknameValidationResult.Error.OVERFLOW)
        }

        if (input.contains(" ")) {
            return NicknameValidationResult.Invalid(NicknameValidationResult.Error.WHITESPACE)
        }

        if (input.matches(ONLY_CONSONANT_REGEX)) {
            return NicknameValidationResult.Invalid(NicknameValidationResult.Error.ONLY_CONSONANTS)
        }

        if (input.any { it in CONSONANT_CHAR_SET }) {
            return NicknameValidationResult.Invalid(NicknameValidationResult.Error.CONTAINS_CONSONANT)
        }

        if (!input.matches(VALID_CHAR_REGEX)) {
            return NicknameValidationResult.Invalid(NicknameValidationResult.Error.SPECIAL_CHAR)
        }

        if (input.matches(ONLY_NUMBER_REGEX)) {
            return NicknameValidationResult.Invalid(NicknameValidationResult.Error.ONLY_NUMBERS)
        }

        return NicknameValidationResult.Valid
    }

    companion object {
        private const val MIN_LENGTH = 2
        private const val MAX_LENGTH = 20
        private val VALID_CHAR_REGEX = Regex("^[가-힣a-zA-Z0-9]*$")
        private val ONLY_NUMBER_REGEX = Regex("^[0-9]+$")
        private val ONLY_CONSONANT_REGEX = Regex("^[ㄱ-ㅎ]+$")
        private val CONSONANT_CHAR_SET = buildSet {
            addAll('ㄱ'..'ㅎ')
            addAll(
                listOf(
                    'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ',
                    'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ',
                    'ㅛ',
                    'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ',
                    'ㅠ',
                    'ㅡ', 'ㅢ',
                    'ㅣ',
                )
            )
        }
    }
}
