package com.napzak.market.banner.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BannerResponse(
    @SerialName("bannerId")
    val bannerId: Long,
    @SerialName("bannerPhoto")
    val bannerPhoto: String,
    @SerialName("bannerUrl")
    val bannerUrl: String,
    @SerialName("bannerSequence")
    val bannerSequence: Int,
    @SerialName("isExternal")
    val isExternal: Boolean,
)
