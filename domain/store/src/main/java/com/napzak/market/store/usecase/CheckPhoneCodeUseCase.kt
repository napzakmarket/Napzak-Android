package com.napzak.market.store.usecase

import com.napzak.market.store.model.PhoneCodeVerificationResult
import com.napzak.market.store.repository.StoreRepository
import javax.inject.Inject

class CheckPhoneCodeUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) {
    suspend operator fun invoke(
        phoneNumber: String,
        code: String
    ): Result<PhoneCodeVerificationResult> =
        storeRepository.checkPhoneVerificationCode(phoneNumber, code)
}
