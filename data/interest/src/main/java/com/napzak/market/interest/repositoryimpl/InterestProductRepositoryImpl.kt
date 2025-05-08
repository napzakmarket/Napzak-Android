package com.napzak.market.interest.repositoryimpl

import com.napzak.market.interest.datasource.InterestProductDataSource
import com.napzak.market.interest.repository.InterestProductRepository
import javax.inject.Inject

class InterestProductRepositoryImpl @Inject constructor(
    private val interestProductDataSource: InterestProductDataSource,
) : InterestProductRepository {
    override suspend fun setInterestProduct(
        productId: Long,
    ): Result<Unit> = runCatching {
        interestProductDataSource.setInterestProduct(productId)
    }

    override suspend fun unsetInterestProduct(
        productId: Long,
    ): Result<Unit> = runCatching {
        interestProductDataSource.deleteInterestProduct(productId)
    }
}
