package com.napzak.market.store.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoreDetailResponse(
    @SerialName("storeId") val storeId: Long,
    @SerialName("storeNickName") val storeNickname: String,
    @SerialName("storeDescription") val storeDescription: String,
    @SerialName("storePhoto") val storePhoto: String,
    @SerialName("storeCover") val storeCover: String,
    @SerialName("isStoreOwner") val isStoreOwner: Boolean,
    @SerialName("genrePreferences") val genrePreferences: List<StoreDetailGenreDto>,
)

@Serializable
data class StoreDetailGenreDto(
    @SerialName("genreId") val genreId: Long,
    @SerialName("genreName") val genreName: String,
)