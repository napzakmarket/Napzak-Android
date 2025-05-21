package com.napzak.market.registration.genre

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.napzak.market.common.state.UiState
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.genre.model.Genre
import com.napzak.market.registration.event.GenreEventBus
import com.napzak.market.registration.genre.component.GenreSearchEmptyView
import com.napzak.market.registration.genre.component.GenreSearchHeader
import com.napzak.market.registration.genre.state.GenreContract.GenreSearchUiState
import com.napzak.market.util.android.throttledNoRippleClickable
import com.napzak.market.util.common.openUrl

private const val GENRE_REQUEST_URL = "https://form.typeform.com/to/C0E09Ymd"

@Composable
fun GenreSearchRoute(
    navigateToUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GenreSearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchTerm by viewModel.searchTerm.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.updateSelectedGenre()
    }

    GenreSearchScreen(
        onBackClick = navigateToUp,
        uiState = uiState,
        searchTerm = searchTerm,
        onSearchTermChange = viewModel::updateSearchTerm,
        onGenreSelect = { genre ->
            GenreEventBus.selectGenre(genre)
            navigateToUp()
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GenreSearchScreen(
    onBackClick: () -> Unit,
    uiState: GenreSearchUiState,
    searchTerm: String,
    onSearchTermChange: (String) -> Unit,
    onGenreSelect: (Genre) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NapzakMarketTheme.colors.gray10),
    ) {
        LazyColumn(
            modifier = modifier.background(NapzakMarketTheme.colors.gray10),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            stickyHeader {
                GenreSearchHeader(
                    onBackClick = onBackClick,
                    searchTerm = searchTerm,
                    onSearchTermChange = onSearchTermChange,
                )
            }
            when {
                uiState.genres.isEmpty() && uiState.loadState != UiState.Loading -> {
                    item {
                        GenreSearchEmptyView(
                            onRequestClick = { context.openUrl(GENRE_REQUEST_URL) },
                        )
                    }
                }

                uiState.genres.isNotEmpty() -> {
                    itemsIndexed(
                        items = uiState.genres,
                        key = { _, genre -> genre.genreId },
                    ) { index, genre ->
                        Row(
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 4.dp),
                        ) {
                            val textStyle =
                                if (uiState.selectedGenreId == genre.genreId) NapzakMarketTheme.typography.body14sb.copy(
                                    color = NapzakMarketTheme.colors.purple500,
                                )
                                else NapzakMarketTheme.typography.body14r.copy(
                                    color = NapzakMarketTheme.colors.gray400,
                                )

                            Text(
                                text = genre.genreName,
                                style = textStyle,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                                    .throttledNoRippleClickable(
                                        coroutineScope = coroutineScope,
                                        onClick = { onGenreSelect(genre) },
                                    ),
                            )
                        }
                    }
                }

                else -> {
                    item {
                        Spacer(modifier = Modifier.fillMaxHeight())
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun RegistrationGenreSearchScreenPreview() {
    NapzakMarketTheme {
        GenreSearchScreen(
            onBackClick = {},
            uiState = GenreSearchUiState(),
            searchTerm = "",
            onSearchTermChange = {},
            onGenreSelect = {},
        )
    }
}
