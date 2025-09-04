package com.napzak.market.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.store.usecase.CheckAutoLoginUseCase
import com.napzak.market.update.repository.RemoteConfigRepository
import com.napzak.market.update.usecase.CheckAppVersionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkAutoLoginUseCase: CheckAutoLoginUseCase,
    private val checkAppVersionUseCase: CheckAppVersionUseCase,
) : ViewModel() {
    private val _isUpdatePopupVisible = MutableStateFlow<Boolean?>(null)
    val isUpdatePopupVisible = _isUpdatePopupVisible.asStateFlow()

    suspend fun tryAutoLogin(): Result<Unit> {
        return checkAutoLoginUseCase()
    }

    fun updatePopupVisible(isVisible: Boolean) {
        _isUpdatePopupVisible.value = isVisible
    }

    fun checkAppVersion(appVersion: String) = viewModelScope.launch {
        val shouldShow = checkAppVersionUseCase(appVersion)
        updatePopupVisible(shouldShow)
    }
}
