package com.napzak.market.presigned_url.usecase

import com.napzak.market.presigned_url.repository.PresignedUrlRepository
import com.napzak.market.presigned_url.usecase.UploadImageUseCase.Companion.REMOTE_URL_KEY
import javax.inject.Inject

class CompressImageUseCase @Inject constructor(
    private val presignedUrlRepository: PresignedUrlRepository,
) {
    suspend operator fun invoke(
        imageUri: String,
    ): Result<String> {
        return if (imageUri.startsWith(REMOTE_URL_KEY)) Result.success(imageUri)
        else presignedUrlRepository.compressProductImage(imageUri)
    }
}
