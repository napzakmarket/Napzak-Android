package com.napzak.market.genre.model

data class Genre(
    val genreId: Long,
    val genreName: String,
    val genrePhoto: String? = null,
    val nextCursor: Long? = null,
)
