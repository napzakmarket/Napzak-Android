package com.napzak.market.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.login.model.LoginFlowRoute
import com.napzak.market.login.model.LoginUiState
import com.napzak.market.store.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun loginWithKakao(token: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            storeRepository.loginWithKakao(token)
                .onSuccess { response ->
                    Log.d("KakaoLogin", "서버 응답 role: ${response.role}")
                    when (response.role) {
                        "ONBOARDING" -> {
                            Log.d("KakaoLogin", "🟢 온보딩 이동")
                            _uiState.update { it.copy(route = LoginFlowRoute.Terms) }
                        }

                        "STORE" -> {
                            Log.d("KakaoLogin", "🔵 메인 이동")
                            _uiState.update { it.copy(route = LoginFlowRoute.Main) }
                        }

                        else -> {
                            Log.d("KakaoLogin", "❓ 알 수 없는 role: ${response.role}")
                        }
                    }
                }
                .onFailure { e ->
                    Log.e("KakaoLogin", "❌ 로그인 실패: ${e.message}", e)
                }
        }
    }
}