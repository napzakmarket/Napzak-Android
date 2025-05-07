package com.napzak.market.mypage.setting.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.store.model.SettingInfo
import com.napzak.market.store.repository.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingRepository: SettingRepository
) : ViewModel() {

    private val _settingInfo = MutableStateFlow(SettingInfo("", "", "", ""))
    val settingInfo: StateFlow<SettingInfo> = _settingInfo.asStateFlow()

    init {
        fetchSettingInfo()
    }

    private fun fetchSettingInfo() = viewModelScope.launch {
        settingRepository.fetchSettingInfo()
            .onSuccess { _settingInfo.value = it }
            .onFailure { /* 에러 무시 */ }
    }
}