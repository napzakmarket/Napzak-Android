package com.napzak.market.registration.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductPhotoDto(
    @SerialName("photoId")
    val photoId: Long?,
    @SerialName("photoUrl")
    val photoUrl: String,
    @SerialName("sequence")
    val sequence: Int,
)
