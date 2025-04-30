package com.napzak.market.presigned_url.datasource

import com.napzak.market.presigned_url.service.PresignedUrlService
import javax.inject.Inject

class PresignedUrlDataSource @Inject constructor(
    private val presignedUrlService: PresignedUrlService,
) {
    suspend fun getProductPresignedUrl(
        imageTitles: List<String>,
    ) = presignedUrlService.getProductPresignedUrl(imageTitles)

    suspend fun getProfilePresignedUrl(
        imageTitles: List<String>,
    ) = presignedUrlService.getProfilePresignedUrl(imageTitles)
}
