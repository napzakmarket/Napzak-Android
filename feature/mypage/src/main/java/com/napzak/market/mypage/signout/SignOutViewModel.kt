package com.napzak.market.mypage.signout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.store.usecase.WithdrawUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignOutViewModel @Inject constructor(
    private val withdrawUseCase: WithdrawUseCase,
) : ViewModel() {
    private val _sideEffect = MutableSharedFlow<SignOutSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    var signOutReason by mutableStateOf("")
    var signOutDescription by mutableStateOf("")

    fun proceedSignOut() {
        viewModelScope.launch {
            withdrawUseCase(signOutReason, signOutDescription)
                .onSuccess {
                    _sideEffect.emit(SignOutSideEffect.SignOutComplete)
                }
        }
    }

    companion object {
        private const val STORE_ID = "storeId"
    }
}
