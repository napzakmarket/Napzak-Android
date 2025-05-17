package com.napzak.market.store.model

sealed class NicknameValidationResult {
    object Uninitialized : NicknameValidationResult()
    object Valid : NicknameValidationResult()
    data class Invalid(val error: Error) : NicknameValidationResult()

    enum class Error {
        EMPTY,
        WHITESPACE,
        SPECIAL_CHAR,
        ONLY_NUMBERS,
        ONLY_CONSONANTS,
        CONTAINS_CONSONANT,
    }
}