package com.napzak.market.mypage.signout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.store.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WithdrawViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
    private val withdrawUseCase: WithdrawUseCase,
) : ViewModel() {
    private val _sideEffect = MutableSharedFlow<SignOutSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    var withdrawReason by mutableStateOf("")
    var withdrawDescription by mutableStateOf("")

    fun withdrawStore() {
        viewModelScope.launch {
            withdrawUseCase(signOutReason, signOutDescription)
                .onSuccess {
                    _sideEffect.emit(WithdrawSideEffect.WithdrawComplete)
                }
        }
    }
}
