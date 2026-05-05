package com.napzak.market.product.repository

import com.napzak.market.product.model.ProductDetail
import kotlinx.coroutines.flow.Flow

interface ProductDetailRepository {
    suspend fun getProductDetail(productId: Long): Result<ProductDetail>
    suspend fun patchTradeStatus(productId: Long, tradeStatus: String): Result<Unit>
    suspend fun deleteProduct(productId: Long): Result<Unit>
    fun getShowProductStatusTooltipFlow(): Flow<Boolean>
    suspend fun setShowProductStatusTooltip(value: Boolean)
}
