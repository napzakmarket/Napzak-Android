package com.napzak.market.interest.service

import com.napzak.market.remote.model.EmptyDataResponse
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface InterestProductService {
    @POST("interest/{productId}")
    suspend fun setInterestProduct(
        @Path("productId") productId: Long,
    ): EmptyDataResponse

    @DELETE("interest/{productId}")
    suspend fun deleteInterestProduct(
        @Path("productId") productId: Long,
    ): EmptyDataResponse
}
