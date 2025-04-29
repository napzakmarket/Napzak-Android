package com.napzak.market.product.repositoryimpl

import com.napzak.market.product.datasource.ProductDetailDataSource
import com.napzak.market.product.mapper.toDomain
import com.napzak.market.product.model.ProductDetail
import com.napzak.market.product.repository.ProductDetailRepository
import javax.inject.Inject

class ProductDetailRepositoryImpl @Inject constructor(
    private val productDetailDataSource: ProductDetailDataSource,
) : ProductDetailRepository {
    override suspend fun getProductDetail(productId: Long): Result<ProductDetail> =
        runCatching {
            val responseData = productDetailDataSource.getProductDetail(productId).data
            val isInterested = responseData.isInterested

            responseData.toDomain(isInterested = isInterested)
        }


    override suspend fun patchTradeStatus(productId: Long, tradeStatus: String): Result<Unit> =
        runCatching {
            productDetailDataSource.patchTradeStatus(
                productId = productId,
                tradeStatus = tradeStatus,
            )
        }

    override suspend fun deleteProduct(productId: Long): Result<Unit> = runCatching {
        productDetailDataSource.deleteProduct(productId)
    }
}
