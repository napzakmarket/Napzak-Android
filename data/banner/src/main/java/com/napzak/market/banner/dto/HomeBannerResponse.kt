package com.napzak.market.banner.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeBannerResponse(
    @SerialName("TopBannerList")
    val topBannerList: List<BannerResponse>,
    @SerialName("MiddleBannerList")
    val middleBannerList: List<BannerResponse>,
    @SerialName("BottomBannerList")
    val bottomBannerList: List<BannerResponse>,
)
