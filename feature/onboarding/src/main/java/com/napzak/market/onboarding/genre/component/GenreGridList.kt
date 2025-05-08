package com.napzak.market.onboarding.genre.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.onboarding.genre.model.GenreUiModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GenreGridList(
    genres: List<GenreUiModel>,
    onGenreClick: (GenreUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(
                items = genres,
                key = { it.name },
            ) { genre ->
                GenreItem(
                    genreName = genre.name,
                    genreImageUrl = genre.imageUrl,
                    isSelected = genre.isSelected,
                    onClick = { onGenreClick(genre) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            NapzakMarketTheme.colors.white.copy(alpha = 0.9f),
                        ),
                    ),
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GenreGridListPreview() {
    NapzakMarketTheme {
        GenreGridList(
            genres = listOf(
                GenreUiModel(
                    name = "마이멜로디",
                    imageUrl = "",
                    isSelected = true,
                ),
                GenreUiModel(
                    name = "카드캡터체리",
                    imageUrl = "",
                    isSelected = false,
                ),
                GenreUiModel(
                    name = "짱구는 못말려",
                    imageUrl = "",
                    isSelected = true,
                ),
                GenreUiModel(
                    name = "도라에몽",
                    imageUrl = "",
                    isSelected = false,
                ),
                GenreUiModel(
                    name = "포켓몬스터",
                    imageUrl = "",
                    isSelected = false,
                ),
                GenreUiModel(
                    name = "명탐정코난",
                    imageUrl = "",
                    isSelected = true,
                )
            ),
            onGenreClick = {},
            modifier = Modifier
                .fillMaxSize()
                .background(NapzakMarketTheme.colors.white)
                .padding(20.dp)
        )
    }
}