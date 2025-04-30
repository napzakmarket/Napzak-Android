package com.napzak.market.genre.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreItemsResponse(
    @SerialName("genreList") val genreList: List<GenreResponse>,
)
