package com.napzak.market.registration.service

import com.napzak.market.registration.dto.Product
import com.napzak.market.registration.dto.PurchaseRegistrationRequest
import com.napzak.market.remote.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RegistrationService {
    @POST("products/sell")
    suspend fun postSaleRegistration(
        @Body request: PurchaseRegistrationRequest,
    ): BaseResponse<Product>

    @GET("products/sell/modify/{productId}")
    suspend fun getSaleRegistration(
        @Path("productId") productId: Long,
    ): BaseResponse<Product>

    @PUT("products/sell/modify/{productId}")
    suspend fun putSaleRegistration(
        @Path("productId") productId: Long,
        @Body request: PurchaseRegistrationRequest,
    ): BaseResponse<Product>

    @POST("products/buy")
    suspend fun postPurchaseRegistration(
        @Body request: PurchaseRegistrationRequest,
    ): BaseResponse<Product>

    @GET("products/buy/modify/{productId}")
    suspend fun getPurchaseRegistration(
        @Path("productId") productId: Long,
        @Body request: PurchaseRegistrationRequest,
    ): BaseResponse<Product>

    @PUT("products/buy/modify/{productId}")
    suspend fun putPurchaseRegistration(
        @Path("productId") productId: Long,
    ): BaseResponse<Product>
}
