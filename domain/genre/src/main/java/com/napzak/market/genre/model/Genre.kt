package com.napzak.market.genre.model

data class Genre(
    val genreId: Long,
    val genreName: String,
    val genrePhoto: String? = null,
)

fun List<Genre>.extractGenreIds(): List<Long> = this.map { it.genreId }
