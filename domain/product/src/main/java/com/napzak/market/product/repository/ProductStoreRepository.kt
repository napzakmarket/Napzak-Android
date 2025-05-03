package com.napzak.market.product.repository

import com.napzak.market.product.model.ExploreParameters
import com.napzak.market.product.model.Product

interface ProductStoreRepository {
    suspend fun getStoreSellProducts(
        storeId: Long,
        parameters: ExploreParameters,
    ): Result<Pair<Int, List<Product>>>

    suspend fun getStoreBuyProducts(
        storeId: Long,
        parameters: ExploreParameters,
    ): Result<Pair<Int, List<Product>>>
}