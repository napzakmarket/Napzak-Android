package com.napzak.market.product.service

import com.napzak.market.product.dto.ProductGetDetailResponse
import com.napzak.market.remote.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ProductDetailService {
    @GET("products/{productId}")
    suspend fun getProductDetail(
        @Path("productId") productId: Long,
    ): BaseResponse<ProductGetDetailResponse>

    @PATCH("products/{productId}")
    suspend fun patchTradeStatus(
        @Path("productId") productId: Long,
        @Body tradeStatus: String,
    ): BaseResponse<Unit>

    @DELETE("products/{productId}")
    suspend fun deleteProduct(
        @Path("productId") productId: Long,
    ): BaseResponse<Unit>
}
