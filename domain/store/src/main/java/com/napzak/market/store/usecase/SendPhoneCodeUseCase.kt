package com.napzak.market.store.usecase

import com.napzak.market.store.repository.StoreRepository
import javax.inject.Inject

class SendPhoneCodeUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) {
    suspend operator fun invoke(phoneNumber: String): Result<Int> = runCatching {
        return storeRepository.postPhoneVerificationCode(phoneNumber)
    }
}
