package com.napzak.market.detail.component.topbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.napzak.market.designsystem.component.topbar.MenuTopBar
import com.napzak.market.designsystem.theme.NapzakMarketTheme

@Composable
internal fun DetailTopBar(
    onBackClick: () -> Unit,
    onOptionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MenuTopBar(
        title = "",
        onNavigateUp = onBackClick,
        onMenuClick = onOptionClick,
        isShadowed = true,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun DetailTopBarPreview() {
    NapzakMarketTheme {
        DetailTopBar(
            onBackClick = {},
            onOptionClick = {},
        )
    }
}