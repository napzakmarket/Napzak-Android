package com.napzak.market.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R
import com.napzak.market.designsystem.theme.NapzakMarketTheme

@Composable
fun HeartClickSnackBar(
    message: String,
    modifier: Modifier = Modifier,
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                color = NapzakMarketTheme.colors.purple500,
                shape = RoundedCornerShape(50.dp),
                )
            .padding(
                horizontal = 23.dp,
                vertical = 9.5.dp,
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_heartclick_snackbar_12),
                contentDescription = stringResource(id = R.string.heart_click_snackbar_icon_description),
                tint = Color.Unspecified,
            )
            Spacer(modifier = Modifier.width(2.5.dp))
            Text(
                text = message,
                style = NapzakMarketTheme.typography.caption12sb,
                color = NapzakMarketTheme.colors.white,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeartClickSnackBarPreview() {
    NapzakMarketTheme {
        HeartClickSnackBar(
            message = stringResource(id = R.string.heart_click_snackbar_message),
            modifier = Modifier
        )
    }
}