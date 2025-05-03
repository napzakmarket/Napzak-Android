package com.napzak.market.interest.usecase

import com.napzak.market.interest.repository.InterestProductRepository
import javax.inject.Inject

class SetInterestProductUseCase @Inject constructor(
    private val interestProductRepository: InterestProductRepository,
) {
    suspend operator fun invoke(productId: Long, isInterested: Boolean): Result<Unit> {
        return if (isInterested) interestProductRepository.setInterestProduct(productId)
        else interestProductRepository.unsetInterestProduct(productId)
    }
}
