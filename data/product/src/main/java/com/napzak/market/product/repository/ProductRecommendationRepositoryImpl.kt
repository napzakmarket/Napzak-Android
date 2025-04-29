package com.napzak.market.product.repository

import com.napzak.market.product.datasource.ProductRecommendationDataSource
import com.napzak.market.product.mapper.toProducts
import com.napzak.market.product.model.Product
import javax.inject.Inject

class ProductRecommendationRepositoryImpl @Inject constructor(
    private val productRecommendationDataSource: ProductRecommendationDataSource,
) : ProductRecommendationRepository {
    override suspend fun getRecommendedProducts(): Result<Pair<String, List<Product>>> =
        runCatching {
            val responseData = productRecommendationDataSource.getRecommendedProducts().data
            val nickname = responseData.nickname
            val products = responseData.toProducts()
            nickname to products
        }

    override suspend fun getPopularSellProducts(): Result<List<Product>> =
        runCatching {
            val responseData = productRecommendationDataSource.getPopularSellProducts().data
            responseData.toProducts()
        }

    override suspend fun getPopularBuyProducts(): Result<List<Product>> =
        runCatching {
            val responseData = productRecommendationDataSource.getPopularBuyProducts().data
            responseData.toProducts()
        }
}
