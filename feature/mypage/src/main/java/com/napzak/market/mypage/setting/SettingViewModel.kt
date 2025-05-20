package com.napzak.market.mypage.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.store.model.SettingInfo
import com.napzak.market.store.repository.SettingRepository
import com.napzak.market.store.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class SettingViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
    private val storeRepository: StoreRepository,
) : ViewModel() {

    private val _settingInfo = MutableStateFlow(SettingInfo("", "", "", ""))
    val settingInfo: StateFlow<SettingInfo> = _settingInfo.asStateFlow()

    private val _sideEffect = Channel<SettingSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        fetchSettingInfo()
    }

    private fun fetchSettingInfo() = viewModelScope.launch {
        settingRepository.fetchSettingInfo()
            .onSuccess { _settingInfo.value = it }
            .onFailure { throwable ->
                Timber.e(throwable, "설정 정보 가져오기 실패")
            }
    }

    fun signOutUser() = viewModelScope.launch {
        storeRepository.logout()
            .onSuccess {
                _sideEffect.send(SettingSideEffect.OnSignOutComplete)
            }
            .onFailure(Timber::e)
    }
}