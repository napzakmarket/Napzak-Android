package com.napzak.market.onboarding.nickname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.onboarding.nickname.model.NicknameErrorType
import com.napzak.market.onboarding.nickname.model.NicknameUiState
import com.napzak.market.onboarding.nickname.model.NicknameValidationResult
import com.napzak.market.store.usecase.CheckNicknameDuplicationUseCase
import com.napzak.market.store.usecase.SetNicknameUseCase
import com.napzak.market.util.android.getHttpExceptionMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NicknameViewModel @Inject constructor(
    private val checkNicknameDuplicationUseCase: CheckNicknameDuplicationUseCase,
    private val setNicknameUseCase: SetNicknameUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NicknameUiState())
    val uiState: StateFlow<NicknameUiState> = _uiState

    fun onNicknameChanged(nickname: String) {
        val validationResult = validateNickname(nickname)
        _uiState.value = NicknameUiState(
            nickname = nickname,
            validationResult = validationResult,
        )
    }

    private fun validateNickname(input: String): NicknameValidationResult {
        return when {
            input.length < MIN_NICKNAME_LENGTH || input.length > MAX_NICKNAME_LENGTH ->
                NicknameValidationResult.Invalid(NicknameErrorType.EMPTY)

            input.contains(" ") ->
                NicknameValidationResult.Invalid(NicknameErrorType.CONTAINS_WHITESPACE)

            !input.matches(VALID_NICKNAME_REGEX) ->
                NicknameValidationResult.Invalid(NicknameErrorType.CONTAINS_SPECIAL_CHAR)

            input.matches(ONLY_NUMBER_REGEX) ->
                NicknameValidationResult.Invalid(NicknameErrorType.ONLY_NUMBERS)

            else -> NicknameValidationResult.Valid
        }
    }

    fun checkNicknameDuplication() {
        val nickname = _uiState.value.nickname
        if (nickname.isBlank()) return

        viewModelScope.launch {
            val result = checkNicknameDuplicationUseCase(_uiState.value.nickname)

            result
                .onSuccess {
                    _uiState.update {
                        it.copy(isAvailable = true, duplicationError = null, isChecking = false)
                    }
                }
                .onFailure { throwable: Throwable ->
                    _uiState.update {
                        it.copy(
                            isAvailable = false,
                            duplicationError = throwable.getHttpExceptionMessage(),
                            isChecking = false
                        )
                    }
                }
        }
    }

    fun updateNickname(
        onSuccess: () -> Unit,
        onError: () -> Unit,
    ) {
        val nickname = _uiState.value.nickname
        if (nickname.isBlank()) return

        viewModelScope.launch {
            val result = setNicknameUseCase(nickname)
            result
                .onSuccess { onSuccess() }
                .onFailure { onError() }
        }
    }

    companion object {
        private const val MIN_NICKNAME_LENGTH = 2
        private const val MAX_NICKNAME_LENGTH = 20
        private val VALID_NICKNAME_REGEX = Regex("^[a-zA-Z0-9가-힣]*$")
        private val ONLY_NUMBER_REGEX = Regex("^[0-9]+$")
    }
}