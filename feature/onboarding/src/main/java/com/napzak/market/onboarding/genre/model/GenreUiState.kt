package com.napzak.market.onboarding.genre.model

import com.napzak.market.common.state.UiState

data class GenreUiState(
    val genres: List<GenreUiModel> = emptyList(),
    val selectedGenres: List<GenreUiModel> = emptyList(),
    val isLoading: UiState<List<GenreUiModel>> = UiState.Success(emptyList()),
    val searchText: String = "",
) {
    val isCompleted: Boolean
        get() = selectedGenres.isNotEmpty() && selectedGenres.size <= 7
}
