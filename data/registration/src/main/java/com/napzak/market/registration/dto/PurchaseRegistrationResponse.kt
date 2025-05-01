package com.napzak.market.registration.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseRegistrationResponse(
    @SerialName("productId")
    val productId: Long,
    @SerialName("productPhotoList")
    val productPhotoDto: List<ProductPhotoDto>,
    @SerialName("genreName")
    val genreName: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("price")
    val price: Int,
    @SerialName("isPriceNegotiable")
    val isPriceNegotiable: Boolean,
)
