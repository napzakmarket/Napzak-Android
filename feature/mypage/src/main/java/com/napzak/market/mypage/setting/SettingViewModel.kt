package com.napzak.market.mypage.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.mypage.setting.state.SettingUiState
import com.napzak.market.notification.repository.NotificationRepository
import com.napzak.market.notification.usecase.GetNotificationSettingsUseCase
import com.napzak.market.notification.usecase.PatchNotificationSettingsUseCase
import com.napzak.market.store.model.SettingInfo
import com.napzak.market.store.repository.SettingRepository
import com.napzak.market.store.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class SettingViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
    private val logoutUseCase: LogoutUseCase,
    private val getNotificationSettingsUseCase: GetNotificationSettingsUseCase,
    private val patchNotificationSettingsUseCase: PatchNotificationSettingsUseCase,
    private val notificationRepository: NotificationRepository,
) : ViewModel() {

    private val _settingInfoState = MutableStateFlow<UiState<SettingInfo>>(UiState.Loading)
    private val _appNotificationState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val settingUiState: StateFlow<SettingUiState> = combine(
        _settingInfoState,
        _appNotificationState,
    ) { settingInfoState, appNotificationState ->
        SettingUiState(
            settingInfoState = settingInfoState,
            appNotificationState = appNotificationState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = SettingUiState(
            settingInfoState = UiState.Loading,
            appNotificationState = UiState.Loading,
        )
    )

    private val _sideEffect = Channel<SettingSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        fetchSettingInfo()
        fetchAppNotificationSetting()
    }

    private fun fetchSettingInfo() = viewModelScope.launch {
        settingRepository.fetchSettingInfo()
            .onSuccess { _settingInfoState.value = UiState.Success(it) }
            .onFailure { _settingInfoState.value = UiState.Failure(it.message.toString()) }
    }

    fun fetchAppNotificationSetting() = viewModelScope.launch {
        val pushToken = notificationRepository.getPushToken()
        if (pushToken != null) getNotificationSettingsUseCase(pushToken)
            .onSuccess { _appNotificationState.value = UiState.Success(it.allowMessage) }
            .onFailure { _appNotificationState.value = UiState.Failure(it.message.toString()) }
        else Timber.tag("FCM_TOKEN").d("Setting-fetchAppNotificationSetting() : pushToken == null")
    }

    fun updateAppNotificationSetting(allowMessage: Boolean) = viewModelScope.launch {
        val pushToken = notificationRepository.getPushToken()
        if (pushToken != null) patchNotificationSettingsUseCase(pushToken, allowMessage)
            .onSuccess {
                _appNotificationState.value = UiState.Success(allowMessage)
                if (!allowMessage) notificationRepository.updateNotificationModalShown(allowMessage)
            }
            .onFailure(Timber::e)
    }

    fun signOutUser() = viewModelScope.launch {
        logoutUseCase()
            .onSuccess { _sideEffect.send(SettingSideEffect.OnSignOutComplete) }
            .onFailure(Timber::e)
    }
}
