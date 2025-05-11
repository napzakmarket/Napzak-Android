package com.napzak.market.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.local.datastore.TokenDataStore
import com.napzak.market.login.model.LoginFlowRoute
import com.napzak.market.login.model.LoginUiState
import com.napzak.market.store.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val tokenReissuer: AuthRepository,
    private val tokenDataStore: TokenDataStore,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun loginWithKakao(token: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            tokenReissuer.loginWithKakao(token)
                .onSuccess { response ->
                    tokenDataStore.setTokens(
                        accessToken = response.accessToken,
                        refreshToken = response.refreshToken
                    )

                    when (response.role) {
                        "ONBOARDING" -> {
                            _uiState.update { it.copy(route = LoginFlowRoute.Terms) }
                        }

                        "STORE" -> {
                            _uiState.update { it.copy(route = LoginFlowRoute.Main) }
                        }

                        else -> {
                        }
                    }
                }
                .onFailure { e ->
                }
        }
    }
}