package com.napzak.market.mypage.withdraw

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.napzak.market.mixpanel.MixpanelConstants.COMPLETED_WITHDRAWAL
import com.napzak.market.notification.repository.FirebaseRepository
import com.napzak.market.notification.repository.NotificationRepository
import com.napzak.market.notification.usecase.DeletePushTokenUseCase
import com.napzak.market.store.usecase.WithdrawUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class WithdrawViewModel @Inject constructor(
    private val withdrawUseCase: WithdrawUseCase,
    private val deletePushTokenUseCase: DeletePushTokenUseCase,
    private val notificationRepository: NotificationRepository,
    private val firebaseRepository: FirebaseRepository,
    private val mixpanel: MixpanelAPI?,
) : ViewModel() {
    private val _sideEffect = MutableSharedFlow<WithdrawSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    var withdrawReason by mutableStateOf("")
    var withdrawDescription by mutableStateOf("")
    var isWithdrawing by mutableStateOf(false)
        private set

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
        viewModelScope.launch {
            isWithdrawing = true
            withdrawUseCase(withdrawReason, withdrawDescription)
                .onSuccess {
                    mixpanel?.track(COMPLETED_WITHDRAWAL)
                    _sideEffect.emit(WithdrawSideEffect.WithdrawComplete)
                }
                .onFailure {
                    Timber.e("Withdraw failed: $it")
                    isWithdrawing = false
                }
        }
    }
}
