package com.napzak.market.store.usecase

import com.napzak.market.store.repository.StoreRepository
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) {
    suspend operator fun invoke(nickname: String): Result<Unit> {
        return runCatching {
            storeRepository.postRegisterNickname(nickname).getOrThrow()
            storeRepository.patchPhoneVerificationStatus().getOrThrow()
        }
    }
}