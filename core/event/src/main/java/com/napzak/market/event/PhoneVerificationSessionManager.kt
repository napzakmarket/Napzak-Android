package com.napzak.market.event

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 번호 인증 확인 플로우가 실행되었는지를 전역적으로 공유하기 위한 매니저 클래스
 */
@Singleton
class PhoneVerificationSessionManager @Inject constructor() {
    private val _isPhoneChecked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isPhoneChecked = _isPhoneChecked.asStateFlow()

    fun setPhoneChecked(value: Boolean) {
        _isPhoneChecked.update { value }
    }
}