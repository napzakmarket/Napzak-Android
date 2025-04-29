package com.napzak.market.product.repository

import com.napzak.market.product.model.ProductDetail

interface ProductDetailRepository {
    suspend fun getProductDetail(productId: Long): Result<ProductDetail>
    suspend fun patchTradeStatus(productId: Long, tradeStatus: String): Result<Unit>
    suspend fun deleteProduct(productId: Long): Result<Unit>
}
