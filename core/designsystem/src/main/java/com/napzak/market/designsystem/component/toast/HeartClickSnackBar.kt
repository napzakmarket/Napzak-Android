package com.napzak.market.designsystem.component.toast

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.img_heart_click_snack_bar
import com.napzak.market.designsystem.theme.NapzakMarketTheme

@Composable
fun HeartClickSnackBar(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(img_heart_click_snack_bar),
        contentDescription = null,
        modifier = modifier.width(186.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun HeartClickSnackBarPreview() {
    NapzakMarketTheme {
        HeartClickSnackBar(
            modifier = Modifier
        )
    }
}