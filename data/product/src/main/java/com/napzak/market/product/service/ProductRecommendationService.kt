package com.napzak.market.product.service

import com.napzak.market.product.dto.ProductHomeBuyResponse
import com.napzak.market.product.dto.ProductHomeRecommendResponse
import com.napzak.market.product.dto.ProductHomeSellResponse
import com.napzak.market.remote.model.BaseResponse
import retrofit2.http.GET

interface ProductRecommendationService {
    @GET("products/home/recommend")
    suspend fun getRecommendedProducts(): BaseResponse<ProductHomeRecommendResponse>

    @GET("products/home/sell")
    suspend fun getPopularSellProducts(): BaseResponse<ProductHomeSellResponse>

    @GET("products/home/buy")
    suspend fun getPopularBuyProducts(): BaseResponse<ProductHomeBuyResponse>
}
