package com.napzak.market.store.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreRegistrationResponse(
    @SerialName("genreList")
    val genreList: List<GenreDto>,
    @SerialName("nextCursor")
    val nextCursor: Int? = null,
)

@Serializable
data class GenreDto(
    @SerialName("genreId")
    val genreId: Long,
    @SerialName("genreName")
    val genreName: String,
)
