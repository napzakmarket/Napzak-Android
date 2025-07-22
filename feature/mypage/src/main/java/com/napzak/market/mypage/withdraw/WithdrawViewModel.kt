package com.napzak.market.mypage.withdraw

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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class WithdrawViewModel @Inject constructor(
    private val withdrawUseCase: WithdrawUseCase,
) : ViewModel() {
    private val _sideEffect = MutableSharedFlow<WithdrawSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    var withdrawReason by mutableStateOf("")
    var withdrawDescription by mutableStateOf("")
    var isWithdrawing by mutableStateOf(false)
        private set

    fun withdrawStore() {
        viewModelScope.launch {
            isWithdrawing = true
            withdrawUseCase(withdrawReason, withdrawDescription)
                .onSuccess {
                    _sideEffect.emit(WithdrawSideEffect.WithdrawComplete)
                }
                .onFailure(Timber::e)
                .also { isWithdrawing = false }
        }
    }
}
