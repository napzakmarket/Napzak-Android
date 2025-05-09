package com.napzak.market.product.repository

import com.napzak.market.product.model.Product

interface ProductRecommendationRepository {
    suspend fun getRecommendedProducts(): Result<Pair<String, List<Product>>>
    suspend fun getPopularSellProducts(): Result<List<Product>>
    suspend fun getPopularBuyProducts(): Result<List<Product>>
}
