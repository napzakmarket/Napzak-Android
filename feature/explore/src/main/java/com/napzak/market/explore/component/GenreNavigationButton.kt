package com.napzak.market.explore.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.designsystem.R.drawable.ic_right_chevron
import com.napzak.market.util.android.noRippleClickable
import com.napzak.market.feature.explore.R.string.explore_genre

@Composable
fun GenreNavigationButton(
    genreName: String,
    onBlockClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .noRippleClickable(onBlockClick)
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = genreName,
            style = NapzakMarketTheme.typography.caption12sb,
            color = NapzakMarketTheme.colors.gray500,
        )

        Spacer(Modifier.width(6.dp))

        GenreChip()

        Spacer(Modifier.weight(1f))

        Icon(
            imageVector = ImageVector.vectorResource(ic_right_chevron),
            contentDescription = null,
            tint = Color.Unspecified,
        )
    }
}

@Composable
private fun GenreChip() {
    Box(
        modifier = Modifier
            .background(
                color = NapzakMarketTheme.colors.purple500,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = stringResource(explore_genre),
            style = NapzakMarketTheme.typography.caption10sb,
            color = NapzakMarketTheme.colors.white,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GenreButtonBlockPreview(modifier: Modifier = Modifier) {
    NapzakMarketTheme {
        GenreNavigationButton(
            genreName = "산리오",
            onBlockClick = { },
        )
    }
}