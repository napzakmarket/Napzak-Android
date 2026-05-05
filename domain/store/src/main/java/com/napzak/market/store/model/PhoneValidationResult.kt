package com.napzak.market.store.model

sealed class PhoneValidationResult {
    object Uninitialized : PhoneValidationResult()
    object Valid : PhoneValidationResult()
    data class Invalid(val error: Error) : PhoneValidationResult()

    enum class Error {
        EMPTY,
        INVALID_FORMAT,
        INVALID_LENGTH,
    }
}
