package com.napzak.market.store.usecase

import com.napzak.market.store.model.TermsAgreement
import com.napzak.market.store.repository.StoreRepository
import javax.inject.Inject

class CheckTermsAgreementUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) {
    suspend operator fun invoke(): Result<TermsAgreement> {
        return storeRepository.getTermsAgreement()
    }
}