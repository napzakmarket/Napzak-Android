package com.napzak.market.genre.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PreferredGenreItemsResponse(
    @SerialName("genreList") val genreList: List<GenreResponse>,
    @SerialName("nextCursor") val nextCursor: Long? = null,
)
