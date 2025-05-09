package com.napzak.market.product.datasource

import com.napzak.market.product.service.ProductRecommendationService
import javax.inject.Inject

class ProductRecommendationDataSource @Inject constructor(
    private val productRecommendationService: ProductRecommendationService,
) {
    suspend fun getRecommendedProducts() = productRecommendationService.getRecommendedProducts()
    suspend fun getPopularSellProducts() = productRecommendationService.getPopularSellProducts()
    suspend fun getPopularBuyProducts() = productRecommendationService.getPopularBuyProducts()
}
