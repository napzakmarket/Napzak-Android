package com.napzak.market.product.repositoryimpl

import com.napzak.market.product.datasource.ProductInterestDataSource
import com.napzak.market.product.mapper.toProducts
import com.napzak.market.product.model.Product
import com.napzak.market.product.repository.ProductInterestRepository
import javax.inject.Inject

class ProductInterestRepositoryImpl @Inject constructor(
    private val productInterestDataSource: ProductInterestDataSource,
) : ProductInterestRepository {
    override suspend fun getInterestSellProducts(cursor: String?): Result<Pair<List<Product>, String>> =
        runCatching {
            val responseData = productInterestDataSource.getInterestSellProducts(cursor).data
            val cursor = responseData.nextCursor
            val products = responseData.products.toProducts()
            (products to cursor) as Pair<List<Product>, String>
        }

    override suspend fun getInterestBuyProducts(cursor: String?): Result<Pair<List<Product>, String>> =
        runCatching {
            val responseData = productInterestDataSource.getInterestBuyProducts(cursor).data
            val cursor = responseData.nextCursor
            val products = responseData.products.toProducts()
            (products to cursor) as Pair<List<Product>, String>
        }
}
