package com.napzak.market.presigned_url.service

import com.napzak.market.presigned_url.dto.ProductPresignedUrlResponseDto
import com.napzak.market.presigned_url.dto.ProfilePresignedUrlResponseDto
import com.napzak.market.remote.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PresignedUrlService {
    @GET("presigned-url/product")
    suspend fun getProductPresignedUrl(
        @Query("productImages") imageTitles: List<String>,
    ): BaseResponse<ProductPresignedUrlResponseDto>

    @GET("presigned-url/stores")
    suspend fun getProfilePresignedUrl(
        @Query("profileImages") imageTitles: List<String>,
    ): BaseResponse<ProfilePresignedUrlResponseDto>
}
