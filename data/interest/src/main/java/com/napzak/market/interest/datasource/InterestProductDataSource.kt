package com.napzak.market.interest.datasource

import com.napzak.market.interest.service.InterestProductService
import javax.inject.Inject

class InterestProductDataSource @Inject constructor(
    private val interestProductService: InterestProductService,
) {
    suspend fun setInterestProduct(productId: Long) =
        interestProductService.setInterestProduct(productId)

    suspend fun deleteInterestProduct(productId: Long) =
        interestProductService.deleteInterestProduct(productId)
}
