package com.napzak.market.onboarding.genre.model

data class GenreUiState(
    val genres: List<GenreUiModel> = emptyList(),
    val selectedGenres: List<GenreUiModel> = emptyList(),
    val searchText: String = "",
) {
    val isCompleted: Boolean
        get() = selectedGenres.isNotEmpty() && selectedGenres.size <= 7
}
