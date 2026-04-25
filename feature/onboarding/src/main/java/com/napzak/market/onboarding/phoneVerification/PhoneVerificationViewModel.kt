package com.napzak.market.onboarding.phoneVerification

import android.provider.SimPhonebookContract.ElementaryFiles.NAME_MAX_LENGTH
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.onboarding.phoneVerification.model.PhoneVerificationUiState
import com.napzak.market.onboarding.phoneVerification.model.VerificationStatus
import com.napzak.market.store.usecase.ValidateCodeUseCase
import com.napzak.market.store.usecase.ValidateNameUseCase
import com.napzak.market.store.usecase.ValidatePhoneUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneVerificationViewModel @Inject constructor(
    private val validateNameUseCase: ValidateNameUseCase,
    private val validatePhoneUseCase: ValidatePhoneUseCase,
    private val validateCodeUseCase: ValidateCodeUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PhoneVerificationUiState())
    val uiState = _uiState.asStateFlow()

    fun onNameChanged(name: String) {
        val limitedName = name.replace(" ", "").take(NAME_MAX_LENGTH)
        _uiState.update {
            it.copy(
                name = limitedName,
                nameValidation = validateNameUseCase(limitedName),
            )
        }
    }

    fun onPhoneChanged(phone: String) {
        val limitedPhone = phone.replace("-", "").take(PHONE_MAX_LENGTH)
        _uiState.update {
            it.copy(
                phone = limitedPhone,
                phoneValidation = validatePhoneUseCase(limitedPhone),
            )
        }
    }

    fun onCodeChanged(code: String) {
        val limitedCode = code.take(CODE_MAX_LENGTH)
        _uiState.update {
            it.copy(
                code = limitedCode,
                codeValidation = validateCodeUseCase(limitedCode),
            )
        }
    }

    fun requestVerification() {
        val current = _uiState.value

        if (!current.isSendEnabled) return

        // TODO: 서버 API 호출

        _uiState.update {
            it.copy(
                isSend = true,
                verificationStatus = VerificationStatus.REQUESTED,
                remainingTimeSec = 180,
            )
        }

        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch {
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
            _uiState.update {
                it.copy(verificationStatus = VerificationStatus.VERIFIED)
            }
        } else {
            // 실패 시 상태 유지 (REQUESTED)
            // 필요하면 error state 따로 추가
        }
    }

    fun updateAgeChecked(new: Boolean) {
        _uiState.update { it.copy(isAgeChecked = new) }
    }

    companion object {
        private const val NAME_MAX_LENGTH = 20
        private const val PHONE_MAX_LENGTH = 11
        private const val CODE_MAX_LENGTH = 6
    }
}