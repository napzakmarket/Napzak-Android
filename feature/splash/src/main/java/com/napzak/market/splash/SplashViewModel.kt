package com.napzak.market.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.store.usecase.CheckAutoLoginUseCase
import com.napzak.market.update.repository.RemoteConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkAutoLoginUseCase: CheckAutoLoginUseCase,
    private val remoteConfigRepository: RemoteConfigRepository,
) : ViewModel() {
    val isUpdatePopupVisible: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    suspend fun tryAutoLogin(): Result<Unit> {
        return checkAutoLoginUseCase()
    }

    fun updatePopupVisible(isVisible: Boolean) {
        isUpdatePopupVisible.value = isVisible
    }

    fun checkAppVersion(appVersion: String) = viewModelScope.launch {
        val latestVersion = runCatching {
            withTimeoutOrNull(2_000) { remoteConfigRepository.getFirebaseRemoteConfig() }
        }.getOrNull()
        val shouldShow = latestVersion?.let { appVersion.trim() != it.trim() } == true //TODO: 정책 확정 후 비교문 수정
        updatePopupVisible(shouldShow)
    }
}
