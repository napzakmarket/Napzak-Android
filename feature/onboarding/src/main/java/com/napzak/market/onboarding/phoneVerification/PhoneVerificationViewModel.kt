package com.napzak.market.onboarding.phoneVerification

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.onboarding.phoneVerification.model.PhoneVerificationError
import com.napzak.market.onboarding.phoneVerification.model.PhoneVerificationUiState
import com.napzak.market.onboarding.phoneVerification.model.VerificationStatus
import com.napzak.market.onboarding.phoneVerification.util.resolveError
import com.napzak.market.store.usecase.CheckPhoneCodeUseCase
import com.napzak.market.store.usecase.SendPhoneCodeUseCase
import com.napzak.market.store.usecase.ValidateCodeUseCase
import com.napzak.market.store.usecase.ValidateNameUseCase
import com.napzak.market.store.usecase.ValidatePhoneUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneVerificationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val validateName: ValidateNameUseCase,
    private val validatePhone: ValidatePhoneUseCase,
    private val validateCode: ValidateCodeUseCase,
    private val sendCode: SendPhoneCodeUseCase,
    private val checkCodeVerified: CheckPhoneCodeUseCase,
) : ViewModel() {
    val isOnboarding = savedStateHandle.getStateFlow(ONBOARDING_KEY, true)

    private val _uiState = MutableStateFlow(PhoneVerificationUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = Channel<PhoneVerificationSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private var timerJob: Job? = null

    fun onNameChanged(name: String) {
        val limitedName = name.replace(" ", "").take(ValidateNameUseCase.MAX_LENGTH)
        _uiState.update {
            it.copy(
                name = limitedName,
                nameValidation = validateName(limitedName),
                errorState = PhoneVerificationError.None,
            )
        }
        checkError()
    }

    fun onPhoneChanged(phone: String) {
        _uiState.update {
            it.copy(
                phone = phone,
                phoneValidation = validatePhone(phone),
                errorState = PhoneVerificationError.None,
            )
        }
        checkError()
    }

    fun onCodeChanged(code: String) {
        val limitedCode = code.take(ValidateCodeUseCase.MAX_LENGTH)
        _uiState.update {
            it.copy(
                code = limitedCode,
                codeValidation = validateCode(limitedCode),
                errorState = PhoneVerificationError.None,
            )
        }
        checkError()
    }

    private fun checkError() {
        _uiState.update { it.copy(errorState = resolveError(it)) }
    }

    private fun startTimer() {
        stopTimer()
        timerJob = viewModelScope.launch {
            while (_uiState.value.remainingTimeSec > 0) {
                delay(1000)

                if (uiState.value.errorState == PhoneVerificationError.VerificationCodeAttemptsExceeded) {
                    stopTimer()
                    _uiState.update { it.copy(remainingTimeSec = 0) }
                    break
                }

                _uiState.update { it.copy(remainingTimeSec = it.remainingTimeSec - 1) }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    fun requestVerification() = viewModelScope.launch {
        val current = _uiState.value

        if (!current.isSendEnabled) return@launch

        val phoneNumber = current.phone.replace("-", "")
        sendCode(phoneNumber)
            .onSuccess {
                _uiState.update {
                    it.copy(
                        code = "",
                        isSend = true,
                        checkingPhone = phoneNumber,
                        verificationStatus = VerificationStatus.REQUESTED,
                        remainingTimeSec = 180,
                        errorState = PhoneVerificationError.None,
                    )
                }
                _sideEffect.send(PhoneVerificationSideEffect.OnCodeSend)
                startTimer()
            }
            .onFailure { e ->
                _uiState.update { it.copy(errorState = requestVerificationErrorHandler(e)) }
            }
    }

    fun verifyCode() = viewModelScope.launch {
        val current = _uiState.value
        if (!current.isVerifyEnabled) return@launch

        checkCodeVerified(phoneNumber = current.checkingPhone, code = current.code)
            .onSuccess { response ->
                if (response.isPhoneVerified) {
                    stopTimer()
                    _uiState.update {
                        it.copy(
                            remainingTimeSec = 0,
                            checkingPhone = "",
                            verificationStatus = VerificationStatus.VERIFIED,
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(errorState = PhoneVerificationError.InvalidVerificationCode)
                    }
                }
            }
            .onFailure { e ->
                _uiState.update { it.copy(errorState = verifyCodeErrorHandler(e)) }
            }
    }

    fun updateAgeChecked(new: Boolean) {
        _uiState.update { it.copy(isAgeChecked = new) }
    }

    private fun requestVerificationErrorHandler(e: Throwable): PhoneVerificationError {
        val code = e.message ?: ""
        return when {
            code.contains("403") -> PhoneVerificationError.PhoneNotAllowed
            code.contains("409") -> PhoneVerificationError.PhoneAlreadyRegistered
            code.contains("429") -> PhoneVerificationError.VerificationRequestLimitExceeded
            else -> PhoneVerificationError.NetworkError
        }
    }

    private fun verifyCodeErrorHandler(e: Throwable): PhoneVerificationError {
        val code = e.message ?: ""
        return when {
            code.contains("404") -> PhoneVerificationError.InvalidVerificationCode
            code.contains("409") -> PhoneVerificationError.PhoneAlreadyRegistered
            code.contains("429") -> PhoneVerificationError.VerificationCodeAttemptsExceeded
            else -> PhoneVerificationError.NetworkError
        }
    }

    companion object {
        private const val ONBOARDING_KEY = "isOnboarding"
    }
}