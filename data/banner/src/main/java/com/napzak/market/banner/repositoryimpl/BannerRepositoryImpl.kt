package com.napzak.market.banner.repositoryimpl

import com.napzak.market.banner.Banner
import com.napzak.market.banner.datasource.BannerDataSource
import com.napzak.market.banner.toDomain
import com.napzak.market.repository.BannerRepository
import com.napzak.market.type.HomeBannerType
import javax.inject.Inject

class BannerRepositoryImpl @Inject constructor(
    private val bannerDataSource: BannerDataSource,
) : BannerRepository {
    override suspend fun getHomeBanner(): Result<Map<HomeBannerType, List<Banner>>> = runCatching {
        val response = bannerDataSource.getHomeBanner().data
        mapOf(
            HomeBannerType.TOP to response.topBannerList.map { it.toDomain() },
            HomeBannerType.MIDDLE to response.middleBannerList.map { it.toDomain() },
            HomeBannerType.BOTTOM to response.bottomBannerList.map { it.toDomain() },
        )
    }
}
