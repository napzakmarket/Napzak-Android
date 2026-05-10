package com.napzak.market.mypage.withdraw

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.mixpanel.SettingsTracker
import com.napzak.market.mypage.withdraw.contract.WithdrawUiState
import com.napzak.market.mypage.withdraw.type.WithdrawReasonType
import com.napzak.market.notification.repository.FirebaseRepository
import com.napzak.market.notification.repository.NotificationRepository
import com.napzak.market.notification.usecase.DeletePushTokenUseCase
import com.napzak.market.store.usecase.WithdrawUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class WithdrawViewModel @Inject constructor(
    private val withdrawUseCase: WithdrawUseCase,
    private val deletePushTokenUseCase: DeletePushTokenUseCase,
    private val notificationRepository: NotificationRepository,
    private val firebaseRepository: FirebaseRepository,
    private val settingsTracker: SettingsTracker,
) : ViewModel() {
    private val _sideEffect = MutableSharedFlow<WithdrawSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private val _uiState = MutableStateFlow(WithdrawUiState.default)
    val uiState = _uiState.asStateFlow()

    fun deletePushToken() {
        viewModelScope.launch {
            val pushToken = notificationRepository.getPushToken()
            if (pushToken != null) {
                deletePushTokenUseCase(pushToken)
                    .onSuccess { notificationRepository.cleanPushToken() }
                    .onFailure { Timber.e(it) }
                firebaseRepository.deletePushTokenFromFirebase()
            }
        }
    }

    fun withdrawStore() {
        if(_uiState.value.isWithdrawing) return

        viewModelScope.launch {
            setIsWithdrawing(true)
            val title = _uiState.value.reason.reason
            val description = _uiState.value.description
            withdrawUseCase(title, description)
                .onSuccess {
                    settingsTracker.trackCompletedWithdrawal()
                    _sideEffect.emit(WithdrawSideEffect.WithdrawComplete)
                }
                .onFailure {
                    Timber.e("Withdraw failed: $it")
                    setIsWithdrawing(false)
                }
        }
    }

    fun setReason(reason: WithdrawReasonType) {
        _uiState.update {
            it.copy(reason = reason)
        }
    }

    fun setDescription(description: String) {
        _uiState.update {
            it.copy(description = description)
        }
    }

    fun setIsWithdrawing(isWithdrawing: Boolean) {
        _uiState.update {
            it.copy(isWithdrawing = isWithdrawing)
        }
    }
}
