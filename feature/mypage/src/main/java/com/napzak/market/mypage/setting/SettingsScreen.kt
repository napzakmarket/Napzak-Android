package com.napzak.market.mypage.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_arrow_left_9
import com.napzak.market.designsystem.R.drawable.ic_arrow_right_9
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.mypage.R.string.settings_topbar_back_button_description
import com.napzak.market.feature.mypage.R.string.settings_topbar_title
import com.napzak.market.feature.mypage.R.string.settings_section_service_info_title
import com.napzak.market.feature.mypage.R.string.settings_item_notice_title
import com.napzak.market.feature.mypage.R.string.settings_item_terms_of_use_title
import com.napzak.market.feature.mypage.R.string.settings_item_privacy_policy_title
import com.napzak.market.feature.mypage.R.string.settings_item_version_info_title
import com.napzak.market.feature.mypage.R.string.settings_button_logout
import com.napzak.market.feature.mypage.R.string.settings_button_withdraw


@Composable
private fun SettingsTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(NapzakMarketTheme.colors.white),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(36.dp),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(ic_arrow_left_9),
                    contentDescription = stringResource(id = settings_topbar_back_button_description),
                    tint = Color.Unspecified,
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = stringResource(id = settings_topbar_title),
                style = NapzakMarketTheme.typography.body16b,
                color = NapzakMarketTheme.colors.gray400,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.2.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RectangleShape,
                    ambientColor = Color.Black.copy(alpha = 0.5f),
                    spotColor = Color.Black.copy(alpha = 0.5f),
                )
        )
    }
}

@Composable
private fun SettingsScreen(
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
            SettingsTopBar(onBackClick = onBackClick)
        }
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

                SettingItem(stringResource(id = settings_item_notice_title), onClick = onNoticeClick)

                Spacer(modifier = Modifier.height(20.dp))

                SettingItem(stringResource(id = settings_item_terms_of_use_title), onClick = onTermsClick)

                Spacer(modifier = Modifier.height(20.dp))

                SettingItem(stringResource(id = settings_item_privacy_policy_title), onClick = onPrivacyClick)

                Spacer(modifier = Modifier.height(20.dp))

                SettingItem(stringResource(id = settings_item_version_info_title), onClick = onVersionClick)

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
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            showLogoutDialog = true
                        }
                )
            }

            if (showLogoutDialog) {
                LogoutDialog(
                    onConfirm = {
                        showLogoutDialog = false
                        onLogoutConfirm()
                    },
                    onDismiss = {
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
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            onWithdrawClick()
                        }
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


@Composable
private fun SettingItem(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                onClick()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = NapzakMarketTheme.typography.body16b,
            color = NapzakMarketTheme.colors.gray400,
        )
        Icon(
            imageVector = ImageVector.vectorResource(ic_arrow_right_9),
            contentDescription = null,
            tint = Color.Unspecified,
        )
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