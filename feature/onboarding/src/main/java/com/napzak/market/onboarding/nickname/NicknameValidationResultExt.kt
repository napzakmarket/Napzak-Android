package com.napzak.market.onboarding.nickname

import com.napzak.market.feature.onboarding.R
import com.napzak.market.store.model.NicknameValidationResult
import com.napzak.market.store.model.NicknameValidationResult.Error.CONTAINS_CONSONANT
import com.napzak.market.store.model.NicknameValidationResult.Error.EMPTY
import com.napzak.market.store.model.NicknameValidationResult.Error.ONLY_CONSONANTS
import com.napzak.market.store.model.NicknameValidationResult.Error.ONLY_NUMBERS
import com.napzak.market.store.model.NicknameValidationResult.Error.SPECIAL_CHAR
import com.napzak.market.store.model.NicknameValidationResult.Error.WHITESPACE

fun NicknameValidationResult.Error.toMessageRes(): Int = when (this) {
    EMPTY -> R.string.onboarding_error_empty
    WHITESPACE -> R.string.onboarding_error_whitespace
    SPECIAL_CHAR -> R.string.onboarding_error_special_char
    ONLY_NUMBERS -> R.string.onboarding_error_only_numbers
    ONLY_CONSONANTS -> R.string.onboarding_error_only_consonants
    CONTAINS_CONSONANT -> R.string.onboarding_error_only_consonants
}