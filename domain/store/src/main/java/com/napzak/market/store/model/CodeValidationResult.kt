package com.napzak.market.store.model

sealed class CodeValidationResult {
    object Uninitialized : CodeValidationResult()
    object Valid : CodeValidationResult()
    data class Invalid(val error: Error) : CodeValidationResult()

    enum class Error {
        EMPTY,
        INVALID_FORMAT,
    }
}
