package com.napzak.market.mypage.setting.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.napzak.market.mypage.setting.SettingsScreen
import com.napzak.market.mypage.setting.model.SettingViewModel

@Composable
fun SettingsRoute(
    onBackClick: () -> Unit,
    onLogoutConfirm: () -> Unit,
    onWithdrawClick: () -> Unit,
    openWebLink: (String) -> Unit,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val state by viewModel.settingInfo.collectAsState()

    SettingsScreen(
        onBackClick = onBackClick,
        onLogoutConfirm = onLogoutConfirm,
        onWithdrawClick = onWithdrawClick,
        onNoticeClick = { if (state.noticeLink.isNotBlank()) openWebLink(state.noticeLink) },
        onTermsClick = { if (state.termsLink.isNotBlank()) openWebLink(state.termsLink) },
        onPrivacyClick = { if (state.privacyPolicyLink.isNotBlank()) openWebLink(state.privacyPolicyLink) },
        onVersionClick = { if (state.versionInfoLink.isNotBlank()) openWebLink(state.versionInfoLink) },
    )
}