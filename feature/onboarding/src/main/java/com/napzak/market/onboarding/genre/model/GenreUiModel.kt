package com.napzak.market.onboarding.genre.model

data class GenreUiModel(
    val name: String,
    val imageUrl: String,
    val isSelected: Boolean = false,
)