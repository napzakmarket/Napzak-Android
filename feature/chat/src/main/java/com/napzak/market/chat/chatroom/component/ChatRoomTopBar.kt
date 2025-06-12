package com.napzak.market.chat.chatroom.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_arrow_left
import com.napzak.market.designsystem.R.drawable.ic_menu
import com.napzak.market.designsystem.R.string.top_bar_menu
import com.napzak.market.designsystem.R.string.top_bar_navigate_up
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.ui_util.ShadowDirection
import com.napzak.market.ui_util.napzakGradientShadow
import com.napzak.market.ui_util.napzakRippleClickable

@Composable
internal fun ChatRoomTopBar(
    storeName: String,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .napzakGradientShadow(
                height = 4.dp,
                startColor = NapzakMarketTheme.colors.shadowBlack,
                endColor = NapzakMarketTheme.colors.transWhite,
                direction = ShadowDirection.Bottom,
            )
            .padding(top = 34.dp, bottom = 18.dp, start = 18.dp, end = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .napzakRippleClickable(
                    role = Role.Button,
                    onClick = onBackClick,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(ic_arrow_left),
                contentDescription = stringResource(top_bar_navigate_up),
                tint = NapzakMarketTheme.colors.black,
            )
        }
        Text(
            text = storeName,
            style = NapzakMarketTheme.typography.body16b,
            color = NapzakMarketTheme.colors.black,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = ImageVector.vectorResource(ic_menu),
            contentDescription = stringResource(top_bar_menu),
            tint = NapzakMarketTheme.colors.black,
            modifier = Modifier
                .napzakRippleClickable(
                    role = Role.Button,
                    onClick = onMenuClick,
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatRoomTopBarPreview() {
    NapzakMarketTheme {
        ChatRoomTopBar(
            storeName = "마이린",
            onBackClick = {},
            onMenuClick = {},
        )
    }
}