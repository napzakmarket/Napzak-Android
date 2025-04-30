package com.napzak.market.genre

data class Genre (
    val genreId: Long,
    val genreName: String,
    val genreImgUrl: String? = null,
)