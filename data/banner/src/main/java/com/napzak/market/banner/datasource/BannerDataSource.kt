package com.napzak.market.banner.datasource

import com.napzak.market.banner.dto.HomeBannerResponse
import com.napzak.market.banner.service.BannerService
import com.napzak.market.remote.model.BaseResponse
import javax.inject.Inject

class BannerDataSource @Inject constructor(
    private val bannerService: BannerService,
) {
    suspend fun getHomeBanner(): BaseResponse<HomeBannerResponse> = bannerService.getHomeBanner()
}