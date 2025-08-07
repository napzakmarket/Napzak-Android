package com.napzak.market.designsystem.component.topbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.napzak.market.designsystem.theme.NapzakMarketTheme

@Composable
fun NavigateUpTopBar(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    isShadowed: Boolean = true,
    paddingValues: PaddingValues = NapzakTopBarDefault.topBarInnerPadding,
    topBarColor: NapzakTopBarColor = NapzakTopBarDefault.topBarColor()
) {
    NapzakBasicTopBar(
        onNavigateUp = onNavigateUp,
        title = title,
        isShadowed = isShadowed,
        color = topBarColor,
        paddingValues = paddingValues,
        modifier = modifier,
        actions = null,
        titleAlign = null,
    )
}

@Preview(showBackground = true)
@Composable
private fun NavigateUpTopBarPreview() {
    NapzakMarketTheme {
        Column {
            NavigateUpTopBar(
                title = "테스트",
                onNavigateUp = {},
            )
        }
    }
}
