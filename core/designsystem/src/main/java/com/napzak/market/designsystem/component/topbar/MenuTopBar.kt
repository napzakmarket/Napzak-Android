package com.napzak.market.designsystem.component.topbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_chevron_left_24
import com.napzak.market.designsystem.R.drawable.ic_menu
import com.napzak.market.designsystem.theme.NapzakMarketTheme

@Composable
fun MenuTopBar(
    title: String,
    onNavigateUp: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    titleAlign: TextAlign = TextAlign.Start,
    isShadowed: Boolean = true,
    paddingValues: PaddingValues = NapzakTopBarDefault.topBarInnerPadding,
    topBarColor: NapzakTopBarColor = NapzakTopBarDefault.topBarColor(),
) {
    val actions = listOf(
        NapzakTopBarAction(iconRes = ic_menu, onClick = onMenuClick),
    )

    val navigators = listOf(
        NapzakTopBarAction(
            iconRes = ic_chevron_left_24,
            onClick = onNavigateUp,
        )
    )

    NapzakBasicTopBar(
        title = title,
        titleAlign = titleAlign,
        isShadowed = isShadowed,
        actions = actions,
        color = topBarColor,
        paddingValues = paddingValues,
        navigators = navigators,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun MenuTopBarPreview() {
    NapzakMarketTheme {
        MenuTopBar(
            title = "테스트",
            onNavigateUp = {},
            onMenuClick = {},
            paddingValues = PaddingValues(start = 13.dp, end = 13.dp, top = 34.dp, bottom = 18.dp),
        )
    }
}
