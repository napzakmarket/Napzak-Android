package com.napzak.market.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.util.android.noRippleClickable

@Composable
fun GenreListItem(
    isSelected: Boolean,
    genreName: String,
    onGenreItemClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val textStyle =
        if (isSelected) NapzakMarketTheme.typography.body14sb
        else NapzakMarketTheme.typography.body14r
    val textColor =
        if (isSelected) NapzakMarketTheme.colors.purple500
        else NapzakMarketTheme.colors.gray400

    Text(
        text = genreName,
        style = textStyle,
        color = textColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp)
            .noRippleClickable(onGenreItemClick),
    )
}

@Preview
@Composable
private fun GenreListItemPrivate(modifier: Modifier = Modifier) {
    NapzakMarketTheme {
        GenreListItem(
            isSelected = false,
            genreName = "산리오산리오산리오",
            onGenreItemClick = {},
            modifier = modifier,
        )
    }
}
