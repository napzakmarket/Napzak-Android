package com.napzak.market.registration.dto

import com.napzak.market.common.type.ProductConditionType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaleRegistrationResponse(
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
    @SerialName("productCondition")
    val productCondition: ProductConditionType,
    @SerialName("isDeliveryIncluded")
    val isDeliveryIncluded: Boolean,
    @SerialName("standardDeliveryFee")
    val standardDeliveryFee: Int,
    @SerialName("halfDeliveryFee")
    val halfDeliveryFee: Int,
)
