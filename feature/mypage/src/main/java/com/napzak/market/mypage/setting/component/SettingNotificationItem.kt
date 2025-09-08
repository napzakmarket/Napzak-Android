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
import com.napzak.market.designsystem.component.button.NapzakToggleButtonDefault
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.mypage.R.string.settings_section_app_notification_off_title
import com.napzak.market.feature.mypage.R.string.settings_section_app_notification_title

@Composable
fun SettingNotificationItem(
    isAppNotificationOn: Boolean,
    isSystemPermissionOn: Boolean,
    onToggleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val text =
        if (!isSystemPermissionOn) stringResource(settings_section_app_notification_off_title)
        else stringResource(settings_section_app_notification_title)
    val textColor =
        if (!isSystemPermissionOn) NapzakMarketTheme.colors.transRed
        else NapzakMarketTheme.colors.gray400
    val toggleColor =
        if (!isSystemPermissionOn) NapzakMarketTheme.colors.purple200
        else NapzakMarketTheme.colors.purple500

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = text,
            style = NapzakMarketTheme.typography.body16m.copy(textColor),
        )

        NapzakToggleButton(
            isToggleOn = isAppNotificationOn,
            onToggleClick = onToggleClick,
            toggleButtonColor = NapzakToggleButtonDefault.color.copy(
                toggleOnColor = toggleColor
            )
        )
    }

}

@Preview
@Composable
fun SettingNotificationItemPreview(modifier: Modifier = Modifier) {
    SettingNotificationItem(
        isAppNotificationOn = false,
        isSystemPermissionOn = true,
        onToggleClick = {},
        modifier = modifier,
    )
}
