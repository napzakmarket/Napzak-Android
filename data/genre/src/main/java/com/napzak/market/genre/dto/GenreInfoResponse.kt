package com.napzak.market.genre.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreInfoResponse(
    @SerialName("genreId") val genreId: Long,
    @SerialName("genreName") val genreName: String,
    @SerialName("tag") val tag: String?,
    @SerialName("cover") val cover: String,
)
