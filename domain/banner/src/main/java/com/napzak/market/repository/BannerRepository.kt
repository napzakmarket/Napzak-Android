package com.napzak.market.repository

import com.napzak.market.banner.Banner
import com.napzak.market.type.HomeBannerType

interface BannerRepository {
    suspend fun getHomeBanner(): Result<Map<HomeBannerType, List<Banner>>>
}