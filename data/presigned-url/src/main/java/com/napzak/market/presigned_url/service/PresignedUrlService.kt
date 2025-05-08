package com.napzak.market.presigned_url.service

import com.napzak.market.presigned_url.dto.ProductPresignedUrlResponse
import com.napzak.market.presigned_url.dto.ProfilePresignedUrlResponse
import com.napzak.market.remote.model.BaseResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query
import retrofit2.http.Url

interface PresignedUrlService {
    @GET("presigned-url/product")
    suspend fun getProductPresignedUrl(
        @Query("productImages") imageTitles: List<String>,
    ): BaseResponse<ProductPresignedUrlResponse>

    @GET("presigned-url/stores")
    suspend fun getProfilePresignedUrl(
        @Query("profileImages") imageTitles: List<String>,
    ): BaseResponse<ProfilePresignedUrlResponse>

    @PUT
    suspend fun putViaPresignedUrl(
        @Url presignedUrl: String,
        @Body requestBody: RequestBody,
    ): Response<Unit>
}
