package com.napzak.market.mypage.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.notification.repository.NotificationRepository
import com.napzak.market.notification.usecase.DeletePushTokenUseCase
import com.napzak.market.notification.usecase.GetNotificationSettingsUseCase
import com.napzak.market.notification.usecase.PatchNotificationSettingsUseCase
import com.napzak.market.store.model.SettingInfo
import com.napzak.market.store.repository.SettingRepository
import com.napzak.market.store.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class SettingViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
    private val logoutUseCase: LogoutUseCase,
    private val getNotificationSettingsUseCase: GetNotificationSettingsUseCase,
    private val patchNotificationSettingsUseCase: PatchNotificationSettingsUseCase,
    private val deletePushTokenUseCase: DeletePushTokenUseCase,
    private val notificationRepository: NotificationRepository,
) : ViewModel() {

    private val _settingInfo = MutableStateFlow(SettingInfo("", "", "", ""))
    val settingInfo: StateFlow<SettingInfo> = _settingInfo.asStateFlow()
    private val _isAppNotificationOn = MutableStateFlow(false)
    val isAppNotificationOn: StateFlow<Boolean> = _isAppNotificationOn.asStateFlow()

    private val _sideEffect = Channel<SettingSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        fetchSettingInfo()
        fetchAppNotificationSetting()
    }

    private fun fetchSettingInfo() = viewModelScope.launch {
        settingRepository.fetchSettingInfo()
            .onSuccess { _settingInfo.value = it }
            .onFailure { throwable ->
                Timber.e(throwable, "설정 정보 가져오기 실패")
            }
    }

    fun fetchAppNotificationSetting() = viewModelScope.launch {
        val pushToken = notificationRepository.getPushToken()
        if (pushToken != null) getNotificationSettingsUseCase(pushToken)
            .onSuccess { _isAppNotificationOn.value = it.allowMessage }
            .onFailure { Timber.e(it) }
        else Timber.tag("FCM_TOKEN").d("Setting-fetchAppNotificationSetting() : pushToken == null")
    }

    fun updateAppNotificationSetting(allowMessage: Boolean) = viewModelScope.launch {
        val pushToken = notificationRepository.getPushToken()
        if (pushToken != null) patchNotificationSettingsUseCase(pushToken, allowMessage)
            .onSuccess { _isAppNotificationOn.value = allowMessage }
            .onFailure { Timber.e(it) }
    }

    fun signOutUser() = viewModelScope.launch {
        logoutUseCase()
            .onSuccess { _sideEffect.send(SettingSideEffect.OnSignOutComplete) }
            .onFailure(Timber::e)

        val pushToken = notificationRepository.getPushToken()
        if (pushToken != null) deletePushTokenUseCase(pushToken)
            .onSuccess { notificationRepository.cleanPushToken() }
            .onFailure { Timber.e(it) }
    }
}