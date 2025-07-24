package com.napzak.market.product.datasource

import com.napzak.market.product.service.ProductInterestService
import javax.inject.Inject

class ProductInterestDataSource @Inject constructor(
    private val productInterestService: ProductInterestService,
) {
    suspend fun getInterestSellProducts(cursor: String?) =
        productInterestService.getInterestSellProducts(cursor)

    suspend fun getInterestBuyProducts(cursor: String?) =
        productInterestService.getInterestBuyProducts(cursor)
}
