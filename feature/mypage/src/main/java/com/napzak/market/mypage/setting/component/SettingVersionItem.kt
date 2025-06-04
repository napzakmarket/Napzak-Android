package com.napzak.market.mypage.setting.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.mypage.BuildConfig
import com.napzak.market.feature.mypage.R.string.settings_item_version_info_title

@Composable
fun SettingVersionItem(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(settings_item_version_info_title),
            style = NapzakMarketTheme.typography.body16b,
            color = NapzakMarketTheme.colors.gray400,
        )
        Text(
            text = BuildConfig.VERSION_NAME,
            style = NapzakMarketTheme.typography.body16m,
            color = NapzakMarketTheme.colors.gray400,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingVersionItemPreview() {
    NapzakMarketTheme {
        SettingVersionItem()
    }
}