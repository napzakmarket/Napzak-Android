package com.napzak.market.product.service

import com.napzak.market.product.dto.ProductInterestBuyResponse
import com.napzak.market.product.dto.ProductInterestSellResponse
import com.napzak.market.remote.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductInterestService {
    @GET("products/interest/sell")
    suspend fun getInterestSellProducts(
        @Query("cursor") cursor: String?,
    ): BaseResponse<ProductInterestSellResponse>

    @GET("products/interest/buy")
    suspend fun getInterestBuyProducts(
        @Query("cursor") cursor: String?,
    ): BaseResponse<ProductInterestBuyResponse>
}
