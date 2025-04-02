package com.napzak.market.common.type


// TODO: 추후 버튼 domain으로 변경 예정
data class Genre(
    val genreId: Long,
    val genreName: String,
    val genreImgUrl: String? = null,
)