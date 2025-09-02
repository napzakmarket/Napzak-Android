package com.napzak.market.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.login.model.LoginFlowRoute
import com.napzak.market.login.model.LoginUiState
import com.napzak.market.store.usecase.SaveTokensUseCase
import com.napzak.market.store.usecase.SetKakaoLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val setKakaoLoginUseCase: SetKakaoLoginUseCase,
    private val saveTokensUseCase: SaveTokensUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun loginWithKakao(token: String) {
        if (_uiState.value.loading) return

        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            setKakaoLoginUseCase(token)
                .onSuccess { response ->
                    runCatching {
                        saveTokensUseCase(
                            accessToken = response.accessToken,
                            refreshToken = response.refreshToken,
                        )
                    }

                    val nextRoute = when (response.role) {
                        "ONBOARDING", "WITHDRAWN" -> LoginFlowRoute.Terms
                        "STORE" -> LoginFlowRoute.Main
                        "REPORTED" -> LoginFlowRoute.Reported
                        else -> null
                    }

                    _uiState.update { it.copy(loading = false, route = nextRoute) }
                }
                .onFailure { e ->
                    if (e.message?.contains(REPORTED_ERROR_CODE) == true ) {
                        _uiState.update { it.copy(loading = false, route = LoginFlowRoute.Reported) }
                    } else {
                        _uiState.update { it.copy(loading = false) }
                    }
                }
        }
    }

    fun consumeRoute() {
        if (_uiState.value.route != null) {
            _uiState.update { it.copy(route = null) }
        }
    }

    fun onRequireBrowserGuide() {
        //TODO 추후 안내 메세지 추가
    }

    fun onKakaoLoginFailed(t: Throwable) {
        _uiState.update { it.copy(loading = false) }
    }

    companion object {
        private const val REPORTED_ERROR_CODE = "403"
    }
}
