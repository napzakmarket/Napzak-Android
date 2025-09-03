package com.napzak.market.splash

import androidx.lifecycle.ViewModel
import com.napzak.market.store.usecase.CheckAutoLoginUseCase
import com.napzak.market.update.usecase.CheckAppVersionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkAutoLoginUseCase: CheckAutoLoginUseCase,
    private val checkAppVersionUseCase: CheckAppVersionUseCase,
) : ViewModel() {

    suspend fun tryAutoLogin(): Result<Unit> {
        return checkAutoLoginUseCase()
    }

    suspend fun checkAppVersion(): Boolean {
        return checkAppVersionUseCase()
    }

    fun moveToPlayStore() {
        // TODO: 구글 플레이 스토어로 이동
    }
}
