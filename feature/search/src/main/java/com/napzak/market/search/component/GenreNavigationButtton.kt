package com.napzak.market.search.component

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.search.R.string.search_ellipsis
import com.napzak.market.util.android.noRippleClickable
import com.napzak.market.feature.search.R.string.search_genre
import com.napzak.market.feature.search.R.drawable.ic_right_chevron

private const val MAX_LENGTH = 20

@Composable
internal fun GenreNavigationButton(
    genreName: String,
    onBlockClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val genre = if (genreName.length > MAX_LENGTH) stringResource(
        search_ellipsis,
        genreName.take(MAX_LENGTH)
    )
    else genreName

    Row(
        modifier = modifier
            .noRippleClickable(onBlockClick)
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = genre,
            style = NapzakMarketTheme.typography.caption12sb,
            color = NapzakMarketTheme.colors.gray500,
        )

        Spacer(Modifier.width(6.dp))

        GenreLabel()

        Spacer(Modifier.weight(1f))

        Icon(
            imageVector = ImageVector.vectorResource(ic_right_chevron),
            contentDescription = null,
            tint = NapzakMarketTheme.colors.gray200,
        )
    }
}

@Composable
private fun GenreLabel() {
    Box(
        modifier = Modifier
            .background(
                color = NapzakMarketTheme.colors.purple500,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {
        Text(
            text = stringResource(search_genre),
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