package com.napzak.market.onboarding.nickname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.onboarding.nickname.model.NicknameUiState
import com.napzak.market.store.usecase.CheckNicknameDuplicationUseCase
import com.napzak.market.store.usecase.SetNicknameUseCase
import com.napzak.market.store.usecase.ValidateNicknameUseCase
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
    private val validateNicknameUseCase: ValidateNicknameUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NicknameUiState())
    val uiState: StateFlow<NicknameUiState> = _uiState

    fun onNicknameChanged(nickname: String) {
        val limitedNickname = nickname.take(MAX_LENGTH)

        val result = validateNicknameUseCase(limitedNickname)
        _uiState.value = _uiState.value.copy(
            nickname = limitedNickname,
            validationResult = result,
            isAvailable = null,
            duplicationError = null,
        )
    }

    fun checkNicknameDuplication() {
        if (_uiState.value.nickname.isBlank()) return

        viewModelScope.launch {

            checkNicknameDuplicationUseCase(_uiState.value.nickname)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isAvailable = true,
                            duplicationError = null,
                            isChecking = false,
                        )
                    }
                }
                .onFailure { throwable: Throwable ->
                    _uiState.update {
                        it.copy(
                            isAvailable = false,
                            duplicationError = throwable.getHttpExceptionMessage(),
                            isChecking = false,
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
        private const val MAX_LENGTH = 20
    }
}