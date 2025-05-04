package com.napzak.market.product.repositoryimpl

import com.napzak.market.product.datasource.ProductStoreDataSource
import com.napzak.market.product.mapper.toProducts
import com.napzak.market.product.model.ExploreParameters
import com.napzak.market.product.model.Product
import com.napzak.market.product.repository.ProductStoreRepository
import javax.inject.Inject

class ProductStoreRepositoryImpl @Inject constructor(
    private val productStoreDataSource: ProductStoreDataSource,
) : ProductStoreRepository {
    override suspend fun getStoreSellProducts(
        storeId: Long,
        parameters: ExploreParameters,
    ): Result<Pair<Int, List<Product>>> = runCatching {
        val responseData = productStoreDataSource.getStoreSellProducts(
            storeId = storeId,
            parameters = parameters,
        )
        val productCount = responseData.data.productCount ?: 0
        val products = responseData.data.products?.toProducts() ?: emptyList()
        productCount to products
    }

    override suspend fun getStoreBuyProducts(
        storeId: Long,
        parameters: ExploreParameters,
    ): Result<Pair<Int, List<Product>>> = runCatching {
        val responseData = productStoreDataSource.getStoreBuyProducts(
            storeId = storeId,
            parameters = parameters,
        )
        val productCount = responseData.data.productCount ?: 0
        val products = responseData.data.products?.toProducts() ?: emptyList()
        productCount to products
    }
}
