package com.napzak.market.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.store.usecase.CheckAutoLoginUseCase
import com.napzak.market.update.repository.RemoteConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkAutoLoginUseCase: CheckAutoLoginUseCase,
    private val remoteConfigRepository: RemoteConfigRepository,
) : ViewModel() {
    var isUpdatePopupVisible: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    suspend fun tryAutoLogin(): Result<Unit> {
        return checkAutoLoginUseCase()
    }

    fun updatePopupVisible(isVisible: Boolean) {
        isUpdatePopupVisible.value = isVisible
    }

    fun checkAppVersion(appVersion: String) = viewModelScope.launch {
        val latestVersion = remoteConfigRepository.getFirebaseRemoteConfig()
        val isVersionIdentical = appVersion == latestVersion
        updatePopupVisible(!isVersionIdentical)
    }

    fun moveToPlayStore() {
        // TODO: 구글 플레이 스토어로 이동
    }
}
