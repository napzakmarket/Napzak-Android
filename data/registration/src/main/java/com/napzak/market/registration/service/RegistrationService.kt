package com.napzak.market.registration.service

import com.napzak.market.registration.dto.ProductIdDto
import com.napzak.market.registration.dto.PurchaseRegistrationRequest
import com.napzak.market.registration.dto.PurchaseRegistrationResponse
import com.napzak.market.registration.dto.SaleRegistrationRequest
import com.napzak.market.registration.dto.SaleRegistrationResponse
import com.napzak.market.remote.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RegistrationService {
    @POST("products/sell")
    suspend fun postSaleRegistration(
        @Body request: SaleRegistrationRequest,
    ): BaseResponse<ProductIdDto>

    @GET("products/sell/modify/{productId}")
    suspend fun getSaleRegistration(
        @Path("productId") productId: Long,
    ): BaseResponse<SaleRegistrationResponse>

    @PUT("products/sell/modify/{productId}")
    suspend fun putSaleRegistration(
        @Path("productId") productId: Long,
        @Body request: SaleRegistrationRequest,
    ): BaseResponse<ProductIdDto>

    @POST("products/buy")
    suspend fun postPurchaseRegistration(
        @Body request: PurchaseRegistrationRequest,
    ): BaseResponse<ProductIdDto>

    @GET("products/buy/modify/{productId}")
    suspend fun getPurchaseRegistration(
        @Path("productId") productId: Long,
    ): BaseResponse<PurchaseRegistrationResponse>

    @PUT("products/buy/modify/{productId}")
    suspend fun putPurchaseRegistration(
        @Path("productId") productId: Long,
        @Body request: PurchaseRegistrationRequest,
    ): BaseResponse<ProductIdDto>
}
