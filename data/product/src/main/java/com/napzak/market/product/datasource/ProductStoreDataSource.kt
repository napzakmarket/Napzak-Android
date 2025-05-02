package com.napzak.market.product.datasource

import com.napzak.market.product.model.ExploreParameters
import com.napzak.market.product.service.ProductStoreService
import javax.inject.Inject

class ProductStoreDataSource @Inject constructor(
    private val productStoreService: ProductStoreService,
) {
    suspend fun getStoreSellProducts(storeId: Long, parameters: ExploreParameters) =
        productStoreService.getStoreSellProducts(
            storeId = storeId,
            sort = parameters.sort,
            genreIds = parameters.genreIds,
            isOnSale = parameters.isOnSale,
            isUnopened = parameters.isUnopened,
            cursor = parameters.cursor,
        )

    suspend fun getStoreBuyProducts(storeId: Long, parameters: ExploreParameters) =
        productStoreService.getStoreBuyProducts(
            storeId = storeId,
            sort = parameters.sort,
            genreIds = parameters.genreIds,
            isOnSale = parameters.isOnSale,
            cursor = parameters.cursor,
        )
}
