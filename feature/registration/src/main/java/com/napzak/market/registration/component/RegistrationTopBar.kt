package com.napzak.market.registration.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_close_24
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.ui_util.noRippleClickable

@Composable
internal fun RegistrationTopBar(
    title: String,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(color = NapzakMarketTheme.colors.white)
            .padding(top = 32.dp, end = 12.dp, bottom = 20.dp),
    ) {
        Text(
            text = title,
            style = NapzakMarketTheme.typography.body16b.copy(
                color = NapzakMarketTheme.colors.gray500,
            ),
            modifier = Modifier.align(Alignment.Center),
        )
        Icon(
            imageVector = ImageVector.vectorResource(ic_close_24),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .noRippleClickable(onCloseClick),
            tint = Color.Unspecified,
        )
    }
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
