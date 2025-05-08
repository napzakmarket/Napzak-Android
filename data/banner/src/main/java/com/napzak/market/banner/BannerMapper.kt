package com.napzak.market.banner

import com.napzak.market.banner.dto.BannerResponse

fun BannerResponse.toDomain() = with(this) {
    Banner(
        id = bannerId,
        imageUrl = bannerPhoto,
        linkUrl = bannerUrl,
        sequence = bannerSequence,
        isExternal = isExternal,
    )
}