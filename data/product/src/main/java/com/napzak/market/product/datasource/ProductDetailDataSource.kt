package com.napzak.market.product.datasource

import com.napzak.market.product.service.ProductDetailService
import javax.inject.Inject

class ProductDetailDataSource @Inject constructor(
    private val productDetailService: ProductDetailService,
) {
    suspend fun getProductDetail(productId: Long) = productDetailService.getProductDetail(productId)

    suspend fun patchTradeStatus(productId: Long, tradeStatus: String) =
        productDetailService.patchTradeStatus(
            productId = productId,
            tradeStatus = tradeStatus,
        )

    suspend fun deleteProduct(productId: Long) = productDetailService.deleteProduct(productId)
}
