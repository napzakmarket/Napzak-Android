package com.napzak.market.mypage.setting.state

import androidx.compose.runtime.Immutable
import com.napzak.market.common.state.UiState
import com.napzak.market.store.model.SettingInfo

@Immutable
data class SettingUiState(
    val settingInfoState: UiState<SettingInfo>,
    val appNotificationState: UiState<Boolean>,
) {
    val isLoaded: UiState<Unit>
        get() = when {
            settingInfoState is UiState.Loading || appNotificationState is UiState.Loading
                -> UiState.Loading

            settingInfoState is UiState.Success && appNotificationState is UiState.Success
                -> UiState.Success(Unit)

            settingInfoState is UiState.Failure || appNotificationState is UiState.Failure
                -> UiState.Failure("failed to load")

            else -> UiState.Empty
        }
}
