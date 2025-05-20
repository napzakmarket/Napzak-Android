package com.napzak.market.mypage.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.napzak.market.designsystem.component.dialog.NapzakDialog
import com.napzak.market.designsystem.component.topbar.NavigateUpTopBar
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.mypage.R.string.settings_button_logout
import com.napzak.market.feature.mypage.R.string.settings_button_withdraw
import com.napzak.market.feature.mypage.R.string.settings_logout_dialog_cancel_button
import com.napzak.market.feature.mypage.R.string.settings_logout_dialog_confirm_button
import com.napzak.market.feature.mypage.R.string.settings_logout_dialog_title
import com.napzak.market.feature.mypage.R.string.settings_section_service_info_title
import com.napzak.market.feature.mypage.R.string.settings_topbar_title
import com.napzak.market.mypage.setting.component.SettingItem
import com.napzak.market.mypage.setting.type.SettingsMenu
import com.napzak.market.util.android.noRippleClickable

@Composable
fun SettingsRoute(
    onBackClick: () -> Unit,
    onLogoutConfirm: () -> Unit,
    onWithdrawClick: () -> Unit,
    openWebLink: (String) -> Unit,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.settingInfo.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle).collect {
            when (it) {
                is SettingSideEffect.OnSignOutComplete -> onLogoutConfirm()
            }
        }
    }

    SettingsScreen(
        onBackClick = onBackClick,
        onLogoutConfirm = viewModel::signOutUser,
        onWithdrawClick = onWithdrawClick,
        onNoticeClick = { if (state.noticeLink.isNotBlank()) openWebLink(state.noticeLink) },
        onTermsClick = { if (state.termsLink.isNotBlank()) openWebLink(state.termsLink) },
        onPrivacyClick = { if (state.privacyPolicyLink.isNotBlank()) openWebLink(state.privacyPolicyLink) },
        onVersionClick = { if (state.versionInfoLink.isNotBlank()) openWebLink(state.versionInfoLink) },
    )
}

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {},
    onNoticeClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onVersionClick: () -> Unit = {},
    onLogoutConfirm: () -> Unit = {},
    onWithdrawClick: () -> Unit = {},
) {

    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            NavigateUpTopBar(
                title = stringResource(id = settings_topbar_title),
                onNavigateUp = onBackClick,
            )
        },
        modifier = Modifier.systemBarsPadding(),
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(NapzakMarketTheme.colors.white),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = stringResource(id = settings_section_service_info_title),
                    style = NapzakMarketTheme.typography.body14r,
                    color = NapzakMarketTheme.colors.gray400,
                )

                Spacer(modifier = Modifier.height(28.dp))

                SettingsMenu.entries.forEachIndexed { index, menu ->
                    SettingItem(
                        title = stringResource(id = menu.titleResId),
                        onClick = when (menu) {
                            SettingsMenu.NOTICE -> onNoticeClick
                            SettingsMenu.TERMS_OF_USE -> onTermsClick
                            SettingsMenu.PRIVACY_POLICY -> onPrivacyClick
                            SettingsMenu.VERSION_INFO -> onVersionClick
                        }
                    )
                    if (index != SettingsMenu.entries.lastIndex) {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                HorizontalDivider(
                    color = NapzakMarketTheme.colors.gray10,
                    thickness = 7.dp,
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = stringResource(id = settings_button_logout),
                    style = NapzakMarketTheme.typography.body16b,
                    color = NapzakMarketTheme.colors.purple500,
                    modifier = Modifier
                        .fillMaxWidth()
                        .noRippleClickable { showLogoutDialog = true }
                )
            }

            if (showLogoutDialog) {
                NapzakDialog(
                    title = stringResource(id = settings_logout_dialog_title),
                    confirmText = stringResource(id = settings_logout_dialog_confirm_button),
                    dismissText = stringResource(id = settings_logout_dialog_cancel_button),
                    onConfirmClick = {
                        showLogoutDialog = false
                        onLogoutConfirm()
                    },
                    onDismissClick = {
                        showLogoutDialog = false
                    }
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                HorizontalDivider(
                    color = NapzakMarketTheme.colors.gray10,
                    thickness = 7.dp,
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = stringResource(id = settings_button_withdraw),
                    style = NapzakMarketTheme.typography.body16b,
                    color = NapzakMarketTheme.colors.red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .noRippleClickable(onWithdrawClick)
                )
            }
            Spacer(modifier = Modifier.height(28.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(NapzakMarketTheme.colors.gray10),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    NapzakMarketTheme {
        SettingsScreen(
            onBackClick = {},
            onNoticeClick = {},
            onTermsClick = {},
            onPrivacyClick = {},
            onVersionClick = {},
            onLogoutConfirm = {
                println("로그아웃 완료")
            },
            onWithdrawClick = {}
        )
    }
}