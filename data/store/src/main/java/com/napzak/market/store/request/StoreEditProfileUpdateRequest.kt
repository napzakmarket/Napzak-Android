package com.napzak.market.store.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoreEditProfileUpdateRequest(
    @SerialName("storeCover") val storeCover: String,
    @SerialName("storePhoto") val storePhoto: String,
    @SerialName("storeNickName") val storeNickName: String,
    @SerialName("storeDescription") val storeDescription: String,
    @SerialName("preferredGenreList") val preferredGenreList: List<Long>
)