package com.napzak.market.search.state

import androidx.compose.runtime.Immutable
import com.napzak.market.common.state.UiState
import com.napzak.market.genre.model.Genre
import com.napzak.market.genre.model.RecommendedSearchWordGenre.SearchWord

@Immutable
data class SearchUiState(
    val loadState: UiState<SearchRecommendation> = UiState.Loading,
    val searchResults: List<Genre> = emptyList(),
)

data class SearchRecommendation(
    val recommendedSearchWords: List<SearchWord>,
    val recommendedGenres: List<Genre>,
)
