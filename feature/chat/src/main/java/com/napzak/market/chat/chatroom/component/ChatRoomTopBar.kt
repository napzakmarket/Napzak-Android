package com.napzak.market.chat.chatroom.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.napzak.market.designsystem.component.topbar.MenuTopBar
import com.napzak.market.designsystem.theme.NapzakMarketTheme

@Composable
internal fun ChatRoomTopBar(
    storeName: String,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MenuTopBar(
        title = storeName,
        titleAlign = TextAlign.Center,
        onNavigateUp = onBackClick,
        onMenuClick = onMenuClick,
        modifier = modifier,
    )
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