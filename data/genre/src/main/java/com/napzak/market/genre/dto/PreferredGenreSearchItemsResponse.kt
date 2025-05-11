package com.napzak.market.genre.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PreferredGenreSearchItemsResponse(
    @SerialName("genreList") val genreList: List<GenreResponse>,
    @SerialName("nextCursor") val nextCursor: String? = null,
    @SerialName("externalLink") val externalLink: String? = null,
)
