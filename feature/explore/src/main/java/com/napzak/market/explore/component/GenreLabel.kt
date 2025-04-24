package com.napzak.market.explore.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.explore.R.string.explore_genre

@Composable
internal fun GenreLabel(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                color = NapzakMarketTheme.colors.purple500,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(horizontal = 7.dp, vertical = 2.dp),
    ) {
        Text(
            text = stringResource(explore_genre),
            style = NapzakMarketTheme.typography.caption10sb,
            color = NapzakMarketTheme.colors.white,
        )
    }
}

@Preview
@Composable
private fun GenreChipPreview(modifier: Modifier = Modifier) {
    NapzakMarketTheme {
        GenreLabel(
            modifier = modifier,
        )
    }
}