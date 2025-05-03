package com.napzak.market.banner.service

import com.napzak.market.banner.dto.HomeBannerResponse
import com.napzak.market.remote.model.BaseResponse
import retrofit2.http.GET

interface BannerService {
    @GET("banners/home")
    suspend fun getHomeBanner(): BaseResponse<HomeBannerResponse>
}
