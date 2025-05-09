package com.napzak.market.store.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreRegistrationRequest(
    @SerialName("genreIds")
    val genreIds: List<Long>,
)
