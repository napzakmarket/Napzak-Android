package com.napzak.market.store.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.util.common.ellipsis

private const val MAX_LENGTH = 5

@Composable
internal fun GenreChip(
    genreName: String,
    modifier: Modifier = Modifier,
    contentColor: Color = NapzakMarketTheme.colors.purple500,
) {
    val genre = genreName.ellipsis(MAX_LENGTH)

    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = contentColor,
                shape = RoundedCornerShape(size = 50.dp),
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
    ) {
        Text(
            text = genre,
            style = NapzakMarketTheme.typography.caption12sb,
            color = contentColor,
        )
    }
}

@Preview
@Composable
private fun GenreChipPreview(modifier: Modifier = Modifier) {
    NapzakMarketTheme {
        GenreChip(
            genreName = "산리오123",
            modifier = modifier,
        )
    }
}