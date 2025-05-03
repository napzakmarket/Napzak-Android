package com.napzak.market.genre.model

data class GenreSearchItems(
    val genreList: List<Genre>,
    val nextCursor: String? = null,
    val externalLink: String? = null,
)
