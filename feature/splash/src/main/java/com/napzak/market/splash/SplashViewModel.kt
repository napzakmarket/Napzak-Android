package com.napzak.market.splash

import androidx.lifecycle.ViewModel
import com.napzak.market.store.usecase.CheckAutoLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkAutoLoginUseCase: CheckAutoLoginUseCase,
) : ViewModel() {

    suspend fun tryAutoLogin(): Result<Unit> {
        return checkAutoLoginUseCase()
    }
}