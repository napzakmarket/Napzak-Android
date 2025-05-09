package com.napzak.market.search.state

import androidx.compose.runtime.Immutable
import com.napzak.market.common.state.UiState
import com.napzak.market.designsystem.component.bottomsheet.Genre

@Immutable
data class SearchUiState(
    val loadState: UiState<SearchRecommendation> = UiState.Loading,
    val searchResults: List<Genre> = emptyList(),
)

data class SearchRecommendation(
    val recommendedSearchWords: List<SearchWord>,
    val recommendedGenres: List<Genre>,
)

// TODO: 추후 도메인에 data class 사용예정
data class SearchWord(
    val searchWordId: Long,
    val searchWord: String,
)
