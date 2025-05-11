package com.napzak.market.presigned_url.usecase

import com.napzak.market.presigned_url.repository.PresignedUrlRepository
import javax.inject.Inject

class PutViaPresignedUrlUseCase @Inject constructor(
    private val presignedUrlRepository: PresignedUrlRepository,
) {
    suspend operator fun invoke(
        presignedUrl: String,
        imageUri: String,
    ): Result<Unit> = presignedUrlRepository.putViaPresignedUrl(
        presignedUrl = presignedUrl,
        imageUri = imageUri,
    )
}
