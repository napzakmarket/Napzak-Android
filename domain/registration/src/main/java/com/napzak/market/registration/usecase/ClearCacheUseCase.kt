package com.napzak.market.registration.usecase

import com.napzak.market.registration.repository.RegistrationRepository
import javax.inject.Inject

class ClearCacheUseCase @Inject constructor(
    private val registrationRepository: RegistrationRepository,
) {
    suspend operator fun invoke(): Result<Unit> =
        registrationRepository.clearCachedImage()
}
