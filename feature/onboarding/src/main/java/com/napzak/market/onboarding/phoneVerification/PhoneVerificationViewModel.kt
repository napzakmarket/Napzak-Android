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
                currentError = PhoneVerificationError.None,
            )
        }
        checkError()
    }

    fun onPhoneChanged(phone: String) {
        val limitedPhone = phone.replace("-", "").take(ValidatePhoneUseCase.MAX_LENGTH)
        _uiState.update {
            it.copy(
                phone = phone,
                phoneValidation = validatePhone(limitedPhone),
                currentError = PhoneVerificationError.None,
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
                currentError = PhoneVerificationError.None,
            )
        }
        checkError()
    }

    private fun checkError() {
        _uiState.update {
            it.copy(currentError = resolveError(it))
        }
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
                        checkingPhone = phoneNumber,
                        isSend = true,
                        remainingCountForCurrentNumber = 5,
                        verificationStatus = VerificationStatus.REQUESTED,
                        remainingTimeSec = 180,
                    )
                }
                _sideEffect.send(PhoneVerificationSideEffect.OnCodeSend)
                startTimer()
            }
            .onFailure { e ->
                if (e.message != null) {
                    _uiState.update {
                        it.copy(
                            currentError =
                                when {
                                    e.message!!.contains("403") -> PhoneVerificationError.PhoneNotAllowed
                                    e.message!!.contains("409") -> PhoneVerificationError.PhoneAlreadyRegistered
                                    e.message!!.contains("429") -> PhoneVerificationError.VerificationRequestLimitExceeded
                                    else -> PhoneVerificationError.NetworkError
                                }
                        )
                    }
                }
            }
    }

    private fun startTimer() {
        stopTimer()
        timerJob = viewModelScope.launch {
            while (_uiState.value.remainingTimeSec > 0) {
                delay(1000)
                _uiState.update { it.copy(remainingTimeSec = it.remainingTimeSec - 1) }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    fun verifyCode() = viewModelScope.launch {
        val current = _uiState.value
        val remainingCount = current.remainingCountForCurrentNumber

        if (remainingCount == 0) {
            _uiState.update { it.copy(currentError = PhoneVerificationError.VerificationRequestLimitExceeded) }
            return@launch
        }

        if (!current.isVerifyEnabled) return@launch

        checkCodeVerified(phoneNumber = current.checkingPhone, code = current.code)
            .onSuccess { response ->
                if (response.isPhoneVerified) {
                    stopTimer()
                    _uiState.update {
                        it.copy(
                            remainingTimeSec = 0,
                            checkingPhone = "",
                            remainingCountForCurrentNumber = 5,
                            verificationStatus = VerificationStatus.VERIFIED,
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            remainingTimeSec = 0,
                            checkingPhone = "",
                            remainingCountForCurrentNumber = remainingCount - 1,
                            currentError = PhoneVerificationError.InvalidVerificationCode,
                        )
                    }
                }
            }
            .onFailure { e ->
                _uiState.update {
                    it.copy(
                        code = "",
                        remainingCountForCurrentNumber = remainingCount - 1,
                    )
                }

                if (e.message != null) {
                    _uiState.update {
                        it.copy(
                            currentError =
                                when {
                                    e.message!!.contains("404") -> PhoneVerificationError.InvalidVerificationCode
                                    e.message!!.contains("409") -> PhoneVerificationError.PhoneAlreadyRegistered
                                    e.message!!.contains("429") -> PhoneVerificationError.VerificationCodeAttemptsExceeded
                                    else -> PhoneVerificationError.NetworkError
                                }
                        )
                    }
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