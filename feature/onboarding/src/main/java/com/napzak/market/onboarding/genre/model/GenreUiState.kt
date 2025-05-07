package com.napzak.market.onboarding.genre.model

data class GenreUiState(
    val genres: List<GenreUiModel> = emptyList(),
    val searchQuery: String = "",
) {
    val selectedGenres: List<GenreUiModel>
        get() = genres.filter { it.isSelected }

    val isMaxSelected: Boolean
        get() = selectedGenres.size >= 7

    val isCompleted: Boolean
        get() = selectedGenres.isNotEmpty()
}
