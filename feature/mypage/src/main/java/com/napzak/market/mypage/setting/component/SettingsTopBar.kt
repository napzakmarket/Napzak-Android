package com.napzak.market.mypage.setting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.mypage.R.string.settings_topbar_back_button_description
import com.napzak.market.feature.mypage.R.string.settings_topbar_title
import com.napzak.market.ui_util.noRippleClickable

@Composable
internal fun SettingsTopBar(
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
                modifier = Modifier.size(24.dp),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(ic_arrow_left_9),
                    contentDescription = stringResource(id = settings_topbar_back_button_description),
                    modifier = Modifier
                        .noRippleClickable(onBackClick),
                    tint = NapzakMarketTheme.colors.gray200
                )
            }

            Spacer(modifier = Modifier.width(9.dp))

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

@Preview(showBackground = true)
@Composable
private fun SettingsTopBarPreview() {
    NapzakMarketTheme {
        SettingsTopBar(
            onBackClick = {}
        )
    }
}