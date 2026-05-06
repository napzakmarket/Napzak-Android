package com.napzak.market.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.store.usecase.GetPhoneVerificationStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPhoneVerificationStatus: GetPhoneVerificationStatusUseCase,
) : ViewModel() {
    private val _isPhoneVerified = MutableStateFlow(false)
    val isPhoneVerified = _isPhoneVerified.asStateFlow()

    fun checkPhoneVerification() = viewModelScope.launch {
        getPhoneVerificationStatus()
            .onSuccess { isVerified ->
                _isPhoneVerified.value = isVerified
            }
            .onFailure {
                _isPhoneVerified.value = false
            }
    }
}
