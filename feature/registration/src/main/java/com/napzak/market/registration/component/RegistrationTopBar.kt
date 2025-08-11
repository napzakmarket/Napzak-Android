package com.napzak.market.registration.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_close_24
import com.napzak.market.designsystem.component.topbar.NapzakBasicTopBar
import com.napzak.market.designsystem.component.topbar.NapzakTopBarAction
import com.napzak.market.designsystem.component.topbar.NapzakTopBarColor
import com.napzak.market.designsystem.theme.NapzakMarketTheme

@Composable
internal fun RegistrationTopBar(
    title: String,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val actions = listOf(NapzakTopBarAction(ic_close_24, onCloseClick))
    val topBarColor = NapzakTopBarColor(
        containerColor = NapzakMarketTheme.colors.white,
        contentColor = NapzakMarketTheme.colors.gray500,
        iconColor = Color.Unspecified,
    )

    NapzakBasicTopBar(
        title = title,
        actions = actions,
        navigators = null,
        color = topBarColor,
        modifier = modifier,
        isShadowed = true,
        titleAlign = TextAlign.Center,
        paddingValues = PaddingValues(start = 12.dp, top = 32.dp, end = 12.dp, bottom = 20.dp),
    )
}

@Preview
@Composable
private fun RegistrationTopBarPreview() {
    NapzakMarketTheme {
        RegistrationTopBar(
            title = "팔아요 등록",
            onCloseClick = { },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
