package com.napzak.market.onboarding.genre

import com.napzak.market.genre.model.Genre
import com.napzak.market.onboarding.genre.model.GenreUiModel

fun Genre.toUiModel(isSelected: Boolean = false): GenreUiModel {
    return GenreUiModel(
        id = genreId,
        name = genreName,
        imageUrl = genrePhoto,
        isSelected = isSelected,
    )
}