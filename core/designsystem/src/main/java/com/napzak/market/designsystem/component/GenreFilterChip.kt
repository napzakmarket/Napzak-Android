package com.napzak.market.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_down_chevron
import com.napzak.market.designsystem.R.string.genre_filter_genre
import com.napzak.market.designsystem.R.string.genre_filter_genre_extra_count
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.genre.model.Genre
import com.napzak.market.util.android.noRippleClickable

@Composable
fun GenreFilterChip(
    genreList: List<Genre>,
    onChipClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val genreText = if (genreList.isEmpty()) {
        stringResource(genre_filter_genre)
    } else {
        genreList.first().genreName
    }
    val extraCount = genreList.size - 1
    val textColor =
        if (genreList.isEmpty()) NapzakMarketTheme.colors.gray400
        else NapzakMarketTheme.colors.white
    val borderColor =
        if (genreList.isEmpty()) NapzakMarketTheme.colors.gray100
        else NapzakMarketTheme.colors.gray500
    val contentColor =
        if (genreList.isEmpty()) Color.Transparent
        else NapzakMarketTheme.colors.gray500
    val textStyle = NapzakMarketTheme.typography.caption12sb

    Row(
        modifier = modifier
            .noRippleClickable(onChipClick)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(size = 72.dp))
            .background(color = contentColor, shape = RoundedCornerShape(size = 72.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (genreList.isEmpty()) {
            Text(
                text = stringResource(genre_filter_genre),
                style = textStyle,
                color = textColor
            )
        } else {
            Row {
                Text(
                    text = genreText,
                    style = textStyle,
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(60.dp)
                )

                if (extraCount > 0) {
                    Text(
                        text = stringResource(genre_filter_genre_extra_count, extraCount),
                        style = textStyle,
                        color = textColor
                    )
                }
            }
        }

        Spacer(Modifier.width(3.dp))

        Icon(
            imageVector = ImageVector.vectorResource(ic_down_chevron),
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(width = 7.dp, height = 4.dp),
        )
    }
}

@Preview
@Composable
private fun GenreFilterChipPreview() {
    NapzakMarketTheme {
        Column {
            GenreFilterChip(
                genreList = emptyList(),
                onChipClick = { },
            )
            GenreFilterChip(
                genreList = listOf(
                    Genre(0, "산리오산리오산리오산리오"),
                ),
                onChipClick = { },
            )
            GenreFilterChip(
                genreList = listOf(
                    Genre(0, "산리오산리오산리오산리오산리오산리오산리오"),
                    Genre(1, "산리오1"),
                    Genre(2, "산리오2"),
                    Genre(3, "산리오3")
                ),
                onChipClick = { },
            )
        }
    }
}