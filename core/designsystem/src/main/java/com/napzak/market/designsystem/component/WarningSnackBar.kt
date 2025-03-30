package com.napzak.market.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun WarningSnackBar(
    message: String,
    modifier: Modifier = Modifier,
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                color = NapzakMarketTheme.colors.transWhite
            )
            .border(
                width = 1.dp,
                color = NapzakMarketTheme.colors.red,
                shape = RoundedCornerShape(50.dp),
            )
            .padding(
                horizontal = 15.dp,
                vertical = 7.dp,
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_warning_snackbar_12),
                contentDescription = stringResource(id = R.string.warning_snackbar_icon_description),
                tint = Color.Unspecified,
                modifier = Modifier.size(12.dp),
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = message,
                style = NapzakMarketTheme.typography.caption12sb,
                color = NapzakMarketTheme.colors.red,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WarningSnackBarPreview() {
    NapzakMarketTheme {
        WarningSnackBar(
            message = stringResource(id = R.string.warning_snackbar_genre_limit_message),
            modifier = Modifier
        )
    }
}