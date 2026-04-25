package com.napzak.market.store.model

sealed class NameValidationResult {
    object Uninitialized : NameValidationResult()
    object Valid : NameValidationResult()
    data class Invalid(val error: Error) : NameValidationResult()

    enum class Error {
        EMPTY,
        TOO_SHORT,
        TOO_LONG,
        INVALID_FORMAT,
    }
}
