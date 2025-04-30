package com.napzak.market.genre.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    @SerialName("genreId") val genreId: Long,
    @SerialName("genreName") val genreName: String,
    @SerialName("genrePhoto") val genrePhoto: String? = null,
    @SerialName("nextCursor") val nextCursor: Long? = null,
)
