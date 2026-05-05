package com.napzak.market.store.usecase

import com.napzak.market.store.repository.StoreRepository
import javax.inject.Inject

class GetPhoneVerificationStatusUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
){
    suspend operator fun invoke(): Result<Boolean> = runCatching {
        return storeRepository.getPhoneVerificationStatus()
    }
}
