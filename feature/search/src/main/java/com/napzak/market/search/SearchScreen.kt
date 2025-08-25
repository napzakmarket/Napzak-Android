package com.napzak.market.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.napzak.market.common.state.UiState
import com.napzak.market.designsystem.component.loading.NapzakLoadingOverlay
import com.napzak.market.designsystem.component.textfield.SearchTextField
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.designsystem.R.drawable.ic_gray_arrow_left
import com.napzak.market.designsystem.R.drawable.ic_gray_search
import com.napzak.market.feature.search.R.string.search_hint
import com.napzak.market.feature.search.R.string.search_suggested_genre
import com.napzak.market.feature.search.R.string.search_suggested_search_text
import com.napzak.market.genre.model.Genre
import com.napzak.market.genre.model.RecommendedSearchWordGenre.SearchWord
import com.napzak.market.search.component.GenreNavigationButton
import com.napzak.market.search.component.SuggestedGenreCard
import com.napzak.market.search.component.SuggestedKeywordChip
import com.napzak.market.search.state.SearchRecommendation
import com.napzak.market.search.state.SearchUiState
import com.napzak.market.ui_util.ShadowDirection
import com.napzak.market.ui_util.napzakGradientShadow
import com.napzak.market.ui_util.noRippleClickable
import kotlinx.coroutines.delay

const val EMPTY_TEXT = ""

@Composable
internal fun SearchRoute(
    onBackButtonClick: () -> Unit,
    onSearchResultNavigate: (String) -> Unit,
    onGenreDetailNavigate: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchText by viewModel.searchTerm.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.updateRecommendedSearchWordGenres()
    }

    SearchScreen(
        uiState = uiState,
        searchText = searchText,
        onBackButtonClick = onBackButtonClick,
        onTextChange = viewModel::updateSearchTerm,
        onSearchClick = { onSearchResultNavigate(searchText) },
        onRecommendedSearchWordClick = onSearchResultNavigate,
        onRecommendedGenreClick = onGenreDetailNavigate,
        modifier = modifier,
    )
}

@Composable
private fun SearchScreen(
    uiState: SearchUiState,
    searchText: String,
    onBackButtonClick: () -> Unit,
    onTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onRecommendedSearchWordClick: (String) -> Unit,
    onRecommendedGenreClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {

    when (uiState.loadState) {
        is UiState.Loading -> NapzakLoadingOverlay()

        is UiState.Empty -> {
        }

        is UiState.Failure -> {
        }

        is UiState.Success -> {
            with(uiState) {
                SearchSuccessScreen(
                    searchText = searchText,
                    recommendedSearchWords = uiState.loadState.data.recommendedSearchWords,
                    recommendedGenres = uiState.loadState.data.recommendedGenres,
                    searchResultGenres = searchResults,
                    onBackButtonClick = onBackButtonClick,
                    onTextChange = onTextChange,
                    onSearchClick = onSearchClick,
                    onRecommendedSearchWordClick = onRecommendedSearchWordClick,
                    onRecommendedGenreClick = onRecommendedGenreClick,
                    modifier = modifier,
                )
            }
        }
    }
}

@Composable
private fun SearchSuccessScreen(
    searchText: String,
    recommendedSearchWords: List<SearchWord>,
    recommendedGenres: List<Genre>,
    searchResultGenres: List<Genre>,
    onBackButtonClick: () -> Unit,
    onTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onRecommendedSearchWordClick: (String) -> Unit,
    onRecommendedGenreClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NapzakMarketTheme.colors.gray10),
    ) {
        Row(
            modifier = Modifier
                .napzakGradientShadow(
                    height = 4.dp,
                    startColor = NapzakMarketTheme.colors.shadowBlack,
                    endColor = NapzakMarketTheme.colors.transWhite,
                    direction = ShadowDirection.Bottom,
                )
                .background(color = NapzakMarketTheme.colors.white)
                .padding(start = 20.dp, top = 42.dp, end = 20.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(ic_gray_arrow_left),
                contentDescription = null,
                tint = NapzakMarketTheme.colors.gray200,
                modifier = Modifier.noRippleClickable(onBackButtonClick),
            )

            Spacer(Modifier.width(12.dp))

            SearchTextField(
                text = searchText,
                onTextChange = onTextChange,
                hint = stringResource(search_hint),
                onResetClick = { onTextChange(EMPTY_TEXT) },
                onSearchClick = onSearchClick,
                focusRequester = focusRequester,
            )
        }

        if (searchText.isEmpty()) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(top = 14.dp),
            ) {
                SuggestedSearchTextSection(
                    recommendedSearchWords = recommendedSearchWords,
                    onTextChipClick = onRecommendedSearchWordClick,
                )

                Spacer(Modifier.height(46.dp))

                SuggestedGenreSection(
                    genres = recommendedGenres,
                    onGenreCardClick = onRecommendedGenreClick,
                )
            }
        } else {
            LazyColumn {
                item {
                    Spacer(Modifier.height(14.dp))
                }

                if (searchText.isNotEmpty()) {
                    item {
                        BasicResultNavigationButton(
                            searchText = searchText,
                            onButtonClick = onSearchClick,
                        )

                        Spacer(
                            Modifier
                                .fillMaxWidth()
                                .background(color = NapzakMarketTheme.colors.gray10)
                                .height(8.dp),
                        )
                    }
                }

                items(searchResultGenres) { genreItem ->
                    GenreNavigationButton(
                        genreName = genreItem.genreName,
                        onBlockClick = { onRecommendedGenreClick(genreItem.genreId) },
                        modifier = Modifier.background(color = NapzakMarketTheme.colors.white),
                    )

                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .background(color = NapzakMarketTheme.colors.gray10)
                            .height(8.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun BasicResultNavigationButton(
    searchText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onButtonClick)
            .background(color = NapzakMarketTheme.colors.white)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .border(
                    width = 1.dp,
                    color = NapzakMarketTheme.colors.gray10,
                    shape = RoundedCornerShape(50.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(ic_gray_search),
                contentDescription = null,
                tint = NapzakMarketTheme.colors.gray400,
            )
        }

        Spacer(Modifier.width(6.dp))

        Text(
            text = searchText,
            style = NapzakMarketTheme.typography.body14sb,
            color = NapzakMarketTheme.colors.gray500,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SuggestedSearchTextSection(
    recommendedSearchWords: List<SearchWord>,
    onTextChipClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(start = 20.dp, top = 14.dp),
    ) {
        Text(
            text = stringResource(search_suggested_search_text),
            style = NapzakMarketTheme.typography.body14b,
            color = NapzakMarketTheme.colors.gray500,
        )

        Spacer(Modifier.height(20.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            recommendedSearchWords.forEach { searchWord ->
                SuggestedKeywordChip(
                    keyword = searchWord.searchWord,
                    onKeywordChipClick = { onTextChipClick(searchWord.searchWord) },
                )
            }
        }
    }
}

@Composable
private fun SuggestedGenreSection(
    genres: List<Genre>,
    onGenreCardClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Text(
            text = stringResource(search_suggested_genre),
            style = NapzakMarketTheme.typography.body14b,
            color = NapzakMarketTheme.colors.gray500,
        )

        Spacer(Modifier.height(20.dp))

        val rows = genres.chunked(3)
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                rowItems.forEach { genre ->
                    SuggestedGenreCard(
                        genreName = genre.genreName,
                        imgUrl = genre.genrePhoto.toString(),
                        onCardClick = { onGenreCardClick(genre.genreId) },
                        modifier = Modifier.width(100.dp),
                    )
                }

                repeat(3 - rowItems.size) {
                    Box(modifier = Modifier.width(100.dp))
                }
            }

            Spacer(modifier = Modifier.height(13.dp))
        }
    }
}

@Preview
@Composable
private fun SearchScreenPreview(modifier: Modifier = Modifier) {
    var searchText by remember { mutableStateOf("") }

    NapzakMarketTheme {
        SearchScreen(
            uiState = SearchUiState(
                loadState = UiState.Success(
                    SearchRecommendation(
                        emptyList(),
                        emptyList()
                    )
                )
            ),
            searchText = searchText,
            onBackButtonClick = { },
            onTextChange = { searchText = it },
            onSearchClick = { },
            onRecommendedSearchWordClick = { },
            onRecommendedGenreClick = { },
            modifier = modifier,
        )
    }
}