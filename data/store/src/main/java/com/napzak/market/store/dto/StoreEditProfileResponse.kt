package com.napzak.market.store.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoreEditProfileResponse(
    @SerialName("storeCover") val storeCover: String,
    @SerialName("storePhoto") val storePhoto: String,
    @SerialName("storeNickName") val storeNickname: String,
    @SerialName("storeDescription") val storeDescription: String,
    @SerialName("preferredGenreList") val preferredGenres: List<StoreEditGenreDto>
)

@Serializable
data class StoreEditGenreDto(
    @SerialName("genreId") val genreId: Long,
    @SerialName("genreName") val genreName: String
)