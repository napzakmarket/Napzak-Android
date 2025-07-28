package com.napzak.market.registration.genre

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.napzak.market.common.state.UiState
import com.napzak.market.designsystem.component.loading.NapzakLoadingSpinnerOverlay
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.genre.model.Genre
import com.napzak.market.registration.RegistrationViewModel
import com.napzak.market.registration.genre.component.GenreSearchEmptyView
import com.napzak.market.registration.genre.component.GenreSearchHeader
import com.napzak.market.registration.genre.state.GenreContract.GenreSearchUiState
import com.napzak.market.ui_util.nonClickableStickyHeader
import com.napzak.market.ui_util.openUrl
import com.napzak.market.ui_util.throttledNoRippleClickable

private const val GENRE_REQUEST_URL = "https://form.typeform.com/to/C0E09Ymd"

@Composable
fun GenreSearchRoute(
    navigateToUp: () -> Unit,
    parentViewModel: RegistrationViewModel,
    modifier: Modifier = Modifier,
    viewModel: GenreSearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val registrationUiState by parentViewModel.registrationUiState.collectAsStateWithLifecycle()
    val searchTerm by viewModel.searchTerm.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.updateSelectedGenre(registrationUiState.genre?.genreId)
    }

    GenreSearchScreen(
        onBackClick = navigateToUp,
        uiState = uiState,
        searchTerm = searchTerm,
        onSearchTermChange = viewModel::updateSearchTerm,
        onGenreSelect = { genre ->
            parentViewModel.updateGenre(genre)
            navigateToUp()
        },
        modifier = modifier,
    )
}

@Composable
private fun GenreSearchScreen(
    onBackClick: () -> Unit,
    uiState: GenreSearchUiState,
    searchTerm: String,
    onSearchTermChange: (String) -> Unit,
    onGenreSelect: (Genre) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    var headerHeight by remember { mutableStateOf(260.dp) }
    val density = LocalDensity.current
    val verticalArrangement =
        if (uiState.loadState is UiState.Success) Arrangement.spacedBy(10.dp)
        else Arrangement.Top

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NapzakMarketTheme.colors.gray10),
        verticalArrangement = verticalArrangement,
    ) {
        nonClickableStickyHeader {
            GenreSearchHeader(
                onBackClick = onBackClick,
                searchTerm = searchTerm,
                onSearchTermChange = onSearchTermChange,
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        headerHeight = with(density) { coordinates.size.height.toDp() }
                    },
            )
        }

        when (uiState.loadState) {
            is UiState.Empty, is UiState.Failure -> item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight - headerHeight),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    GenreSearchEmptyView(
                        onRequestClick = { context.openUrl(GENRE_REQUEST_URL) },

                        )
                }
            }

            is UiState.Success -> {
                itemsIndexed(
                    items = uiState.loadState.data,
                    key = { _, genre -> genre.genreId },
                ) { index, genre ->
                    val topPadding = if (index == 0) 4.dp else 0.dp
                    val bottomPadding =
                        if (index == uiState.loadState.data.lastIndex) 4.dp else 0.dp

                    Row(
                        modifier = Modifier
                            .padding(
                                top = topPadding,
                                bottom = bottomPadding,
                            )
                            .throttledNoRippleClickable(
                                coroutineScope = coroutineScope,
                                onClick = { onGenreSelect(genre) },
                            )
                            .padding(horizontal = 18.dp),
                    ) {
                        GenreSearchItem(
                            selectedGenreId = uiState.selectedGenreId,
                            genre = genre,
                        )
                    }
                }
            }

            else -> item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight - headerHeight),
                    contentAlignment = Alignment.Center,
                ) {
                    NapzakLoadingSpinnerOverlay()
                }
            }
        }
    }
}

@Composable
private fun GenreSearchItem(
    selectedGenreId: Long?,
    genre: Genre,
    modifier: Modifier = Modifier,
) {
    val textStyle =
        if (selectedGenreId == genre.genreId) NapzakMarketTheme.typography.body14sb.copy(
            color = NapzakMarketTheme.colors.purple500,
        )
        else NapzakMarketTheme.typography.body14r.copy(
            color = NapzakMarketTheme.colors.gray400,
        )

    Text(
        text = genre.genreName,
        style = textStyle,
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
    )
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
