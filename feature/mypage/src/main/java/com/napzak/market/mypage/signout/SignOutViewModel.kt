package com.napzak.market.mypage.signout

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignOutViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val storeId
        get() = savedStateHandle.get<Long>(STORE_ID) ?: 0

    private val _sideEffect = MutableSharedFlow<SignOutSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    var signOutReason by mutableStateOf("")
    var signOutDescription by mutableStateOf("")

    fun proceedSignOut() = viewModelScope.launch {
        Log.d(
            "SignOutViewModel",
            "signOutId: $storeId\nsignOutReason: $signOutReason\nsignOutDescription: $signOutDescription"
        )
        _sideEffect.emit(SignOutSideEffect.SignOutComplete)
    }

    companion object {
        private const val STORE_ID = "storeId"
    }
}
