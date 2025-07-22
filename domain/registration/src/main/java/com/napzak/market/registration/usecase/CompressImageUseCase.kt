package com.napzak.market.registration.usecase

import com.napzak.market.registration.repository.RegistrationRepository
import javax.inject.Inject

private const val REMOTE_URL_KEY = "https://"

class CompressImageUseCase @Inject constructor(
    private val registrationRepository: RegistrationRepository,
) {
    suspend operator fun invoke(
        imageUri: String,
    ): Result<String> {
        return if (imageUri.startsWith(REMOTE_URL_KEY)) Result.success(imageUri)
        else registrationRepository.compressProductImage(imageUri)
    }
}
