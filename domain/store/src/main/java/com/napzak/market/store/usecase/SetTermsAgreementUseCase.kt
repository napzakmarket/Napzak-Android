package com.napzak.market.store.usecase

import com.napzak.market.store.repository.StoreRepository
import javax.inject.Inject

class SetTermsAgreementUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) {
    suspend operator fun invoke(bundleId: Int): Result<Unit> {
        return storeRepository.postTermsAgreement(bundleId)
    }
}