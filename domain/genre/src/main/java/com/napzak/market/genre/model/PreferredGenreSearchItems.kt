package com.napzak.market.genre.model

data class PreferredGenreSearchItems(
    val genreList: List<Genre>,
    val nextCursor: String? = null,
    val externalLink: String? =null,
)
