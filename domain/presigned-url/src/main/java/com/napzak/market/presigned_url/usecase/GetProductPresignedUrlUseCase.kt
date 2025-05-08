package com.napzak.market.presigned_url.usecase

import com.napzak.market.presigned_url.model.PresignedUrl
import com.napzak.market.presigned_url.repository.PresignedUrlRepository
import javax.inject.Inject

class GetProductPresignedUrlUseCase @Inject constructor(
    private val presignedUrlRepository: PresignedUrlRepository
) {
    suspend operator fun invoke(
        imageTitles: List<String>,
    ): Result<List<PresignedUrl>> = presignedUrlRepository.getProductPresignedUrls(
        imageTitles = List(imageTitles.size) { index ->
            "$IMAGE_TITLE_PREFIX${index + 1}"
        }
    )
}

private const val IMAGE_TITLE_PREFIX = "image_"
