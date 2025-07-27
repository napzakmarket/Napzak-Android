package com.napzak.market.mypage.setting.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.napzak.market.designsystem.component.button.NapzakToggleButton
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.mypage.R.string.settings_section_app_notification_off_title
import com.napzak.market.feature.mypage.R.string.settings_section_app_notification_title

@Composable
fun SettingNotificationItem(
    isAppNotificationOn: Boolean,
    onToggleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val text =
        if (isAppNotificationOn) stringResource(settings_section_app_notification_title) else stringResource(
            settings_section_app_notification_off_title
        )
    val textColor =
        if (isAppNotificationOn) NapzakMarketTheme.colors.black else NapzakMarketTheme.colors.red

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = text,
            style = NapzakMarketTheme.typography.body16b.copy(textColor),
            color = NapzakMarketTheme.colors.gray400,
        )

        NapzakToggleButton(
            isToggleOn = isAppNotificationOn,
            onToggleClick = onToggleClick,
        )
    }

}

@Preview
@Composable
fun SettingNotificationItemPreview(modifier: Modifier = Modifier) {
    SettingNotificationItem(
        isAppNotificationOn = true,
        onToggleClick = {},
        modifier = modifier,
    )
}
