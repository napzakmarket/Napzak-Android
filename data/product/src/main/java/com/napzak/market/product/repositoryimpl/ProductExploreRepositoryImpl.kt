package com.napzak.market.product.repositoryimpl

import com.napzak.market.product.datasource.ProductExploreDataSource
import com.napzak.market.product.mapper.toProducts
import com.napzak.market.product.model.ExploreParameters
import com.napzak.market.product.model.Product
import com.napzak.market.product.model.SearchParameters
import com.napzak.market.product.repository.ProductExploreRepository
import javax.inject.Inject

class ProductExploreRepositoryImpl @Inject constructor(
    private val productExploreDataSource: ProductExploreDataSource,
) : ProductExploreRepository {
    override suspend fun getExploreSellProducts(parameters: ExploreParameters): Result<Pair<Int, List<Product>>> =
        runCatching {
            val responseData = productExploreDataSource.getExploreSellProducts(parameters).data
            val productCount = responseData.productCount ?: 0
            val products = responseData.products?.toProducts() ?: emptyList()
            productCount to products
        }

    override suspend fun getExploreBuyProducts(parameters: ExploreParameters): Result<Pair<Int, List<Product>>> =
        runCatching {
            val responseData = productExploreDataSource.getExploreBuyProducts(parameters).data
            val productCount = responseData.productCount ?: 0
            val products = responseData.products?.toProducts() ?: emptyList()
            productCount to products
        }

    override suspend fun getSearchSellProducts(parameters: SearchParameters): Result<Pair<Int, List<Product>>> =
        runCatching {
            val responseData = productExploreDataSource.getSearchSellProducts(parameters).data
            val productCount = responseData.productCount ?: 0
            val products = responseData.products?.toProducts() ?: emptyList()
            productCount to products
        }

    override suspend fun getSearchBuyProducts(parameters: SearchParameters): Result<Pair<Int, List<Product>>> =
        runCatching {
            val responseData = productExploreDataSource.getSearchBuyProducts(parameters).data
            val productCount = responseData.productCount ?: 0
            val products = responseData.products?.toProducts() ?: emptyList()
            productCount to products
        }
}
