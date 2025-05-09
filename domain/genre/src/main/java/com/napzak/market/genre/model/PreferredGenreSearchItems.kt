package com.napzak.market.genre.model

data class PreferredGenreSearchItems(
    val genreList: List<Genre>,
    val nextCursor: Long? = null,
    val externalLink: String? =null,
)
