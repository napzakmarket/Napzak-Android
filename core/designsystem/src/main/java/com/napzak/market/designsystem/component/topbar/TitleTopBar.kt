package com.napzak.market.designsystem.component.topbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.napzak.market.designsystem.theme.NapzakMarketTheme

@Composable
fun TitleTopBar(
    title: String,
    modifier: Modifier = Modifier,
    titleAlign: TextAlign = TextAlign.Start,
    isShadowed: Boolean = true,
    paddingValues: PaddingValues = NapzakTopBarDefault.topBarInnerPadding,
    topBarColor: NapzakTopBarColor = NapzakTopBarDefault.topBarColor(),
) {
    NapzakBasicTopBar(
        title = title,
        titleAlign = titleAlign,
        modifier = modifier,
        paddingValues = paddingValues,
        color = topBarColor,
        isShadowed = isShadowed,
        onNavigateUp = null,
        actions = null,
    )
}

@Preview(showBackground = true)
@Composable
private fun TitleTopBarPreview() {
    NapzakMarketTheme {
        TitleTopBar(title = "제목")
    }
}
