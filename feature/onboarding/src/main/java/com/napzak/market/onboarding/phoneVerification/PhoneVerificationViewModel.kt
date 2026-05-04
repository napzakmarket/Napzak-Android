package com.napzak.market.onboarding.phoneVerification

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.onboarding.phoneVerification.model.PhoneVerificationUiState
import com.napzak.market.onboarding.phoneVerification.model.VerificationStatus
import com.napzak.market.store.usecase.ValidateCodeUseCase
import com.napzak.market.store.usecase.ValidateNameUseCase
import com.napzak.market.store.usecase.ValidatePhoneUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneVerificationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val validateName: ValidateNameUseCase,
    private val validatePhone: ValidatePhoneUseCase,
    private val validateCode: ValidateCodeUseCase,
) : ViewModel() {
    val isOnboarding = savedStateHandle.getStateFlow(ONBOARDING_KEY, true)

    private val _uiState = MutableStateFlow(PhoneVerificationUiState())
    val uiState = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun onNameChanged(name: String) {
        val limitedName = name.replace(" ", "").take(ValidateNameUseCase.MAX_LENGTH)
        _uiState.update {
            it.copy(
                name = limitedName,
                nameValidation = validateName(limitedName),
            )
        }
    }

    fun onPhoneChanged(phone: String) {
        val limitedPhone = phone.replace("-", "").take(ValidatePhoneUseCase.MAX_LENGTH)
        _uiState.update {
            it.copy(
                phone = limitedPhone,
                phoneValidation = validatePhone(limitedPhone),
            )
        }
    }

    fun onCodeChanged(code: String) {
        val limitedCode = code.take(ValidateCodeUseCase.MAX_LENGTH)
        _uiState.update {
            it.copy(
                code = limitedCode,
                codeValidation = validateCode(limitedCode),
            )
        }
    }

    fun requestVerification() {
        val current = _uiState.value

        if (!current.isSendEnabled) return

        // TODO: 서버 API 호출

        _uiState.update {
            it.copy(
                code = "",
                isSend = true,
                verificationStatus = VerificationStatus.REQUESTED,
                remainingTimeSec = 180,
            )
        }
        // TODO: 인증 발송 토스트

        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.remainingTimeSec > 0) {
                delay(1000)
                _uiState.update { it.copy(remainingTimeSec = it.remainingTimeSec - 1) }
            }
        }
    }

    fun verifyCode() {
        val current = _uiState.value

        if (!current.isVerifyEnabled) return

        // TODO: 서버 API 호출
        if (true) {
            timerJob?.cancel()
            _uiState.update {
                it.copy(
                    remainingTimeSec = 0,
                    verificationStatus = VerificationStatus.VERIFIED,
                )
            }
        } else {
            // 실패 시 상태 유지 (REQUESTED)
            // 필요하면 error state 따로 추가
            _uiState.update {
                it.copy(
                    code = "",
                )
            }
        }
    }

    fun updateAgeChecked(new: Boolean) {
        _uiState.update { it.copy(isAgeChecked = new) }
    }

    companion object {
        private const val ONBOARDING_KEY = "isOnboarding"
    }
}