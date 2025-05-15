package com.napzak.market.store.usecase

import com.napzak.market.store.model.NicknameValidationResult
import com.napzak.market.store.type.NicknameValidationErrorType
import javax.inject.Inject

class CheckNicknameValidationUseCase @Inject constructor() {
    operator fun invoke(nickname: String): NicknameValidationResult {
        return when {
            nickname.length < MIN_NICKNAME_LENGTH -> NicknameValidationResult.Empty
            nickname.contains(" ") ->
                NicknameValidationResult.Invalid(NicknameValidationErrorType.CONTAINS_WHITESPACE.message)

            CONSONANT_REGEX.containsMatchIn(nickname) ->
                NicknameValidationResult.Invalid(NicknameValidationErrorType.CONTAINS_CONSONANT.message)

            !nickname.matches(VALID_NICKNAME_REGEX) ->
                NicknameValidationResult.Invalid(NicknameValidationErrorType.CONTAINS_SPECIAL_CHAR.message)

            nickname.matches(ONLY_NUMBER_REGEX) ->
                NicknameValidationResult.Invalid(NicknameValidationErrorType.ONLY_NUMBERS.message)

            else -> NicknameValidationResult.Valid()
        }
    }


    companion object {
        private const val MIN_NICKNAME_LENGTH = 2
        private val CONSONANT_REGEX = Regex("[ㄱ-ㅎㅏ-ㅣ]")
        private val VALID_NICKNAME_REGEX = Regex("^[a-zA-Z0-9가-힣]*$")
        private val ONLY_NUMBER_REGEX = Regex("^[0-9]+$")
    }
}