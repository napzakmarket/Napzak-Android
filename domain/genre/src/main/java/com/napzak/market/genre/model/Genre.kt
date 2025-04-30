package com.napzak.market.genre.model

data class Genre (
    val genreId: Long,
    val genreName: String,
    val genreImgUrl: String? = null,
)