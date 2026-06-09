package com.napzak.market.store.usecase

import com.napzak.market.store.repository.StoreRepository
import javax.inject.Inject

class UpdateUserVerificationStatusUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) {
    suspend operator fun invoke(): Result<Unit> {
        return storeRepository.patchPhoneVerificationStatus()
    }
}
