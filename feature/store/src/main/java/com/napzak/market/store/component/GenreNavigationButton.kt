package com.napzak.market.store.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.util.android.noRippleClickable
import com.napzak.market.feature.store.R.string.market_genre
import com.napzak.market.feature.store.R.drawable.ic_right_chevron

@Composable
internal fun GenreNavigationButton(
    genreName: String,
    onBlockClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .noRippleClickable(onBlockClick)
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = genreName,
                style = NapzakMarketTheme.typography.caption12sb,
                color = NapzakMarketTheme.colors.gray500,
            )
        }

        Spacer(Modifier.width(6.dp))

        GenreChip()

        Icon(
            imageVector = ImageVector.vectorResource(ic_right_chevron),
            contentDescription = null,
            tint = NapzakMarketTheme.colors.gray200,
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
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {
        Text(
            text = stringResource(market_genre),
            style = NapzakMarketTheme.typography.caption10sb,
            color = NapzakMarketTheme.colors.white,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GenreButtonBlockPreview() {
    NapzakMarketTheme {
        GenreNavigationButton(
            genreName = "산리오산리오산리오산리오산리오산리오산리오산리오산리오산리오",
            onBlockClick = { },
        )
    }
}