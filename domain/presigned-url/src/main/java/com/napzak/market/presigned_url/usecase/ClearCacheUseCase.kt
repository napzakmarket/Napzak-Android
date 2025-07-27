package com.napzak.market.presigned_url.usecase

import com.napzak.market.presigned_url.repository.PresignedUrlRepository
import javax.inject.Inject

class ClearCacheUseCase @Inject constructor(
    private val presignedUrlRepository: PresignedUrlRepository,
) {
    operator fun invoke(): Result<Unit> =
        presignedUrlRepository.clearCachedImage()
}
