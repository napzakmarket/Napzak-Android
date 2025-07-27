package com.napzak.market.presigned_url.usecase

import com.napzak.market.presigned_url.model.PresignedUrl
import com.napzak.market.presigned_url.repository.PresignedUrlRepository
import com.napzak.market.presigned_url.usecase.UploadImageUseCase.Companion.IMAGE_TITLE_PREFIX
import com.napzak.market.presigned_url.usecase.UploadImageUseCase.Companion.REMOTE_URL_KEY
import javax.inject.Inject

class GetProductPresignedUrlUseCase @Inject constructor(
    private val presignedUrlRepository: PresignedUrlRepository,
) {
    suspend operator fun invoke(
        images: List<Pair<Int, String>>,
    ): Result<List<PresignedUrl>> {
        val (remoteImages, localImages) = images.partition { (_, uri) ->
            uri.startsWith(REMOTE_URL_KEY)
        }

        val localPresignedResult = localImages
            .takeIf { it.isNotEmpty() }
            ?.let { images ->
                val titles = images.map { (index, _) ->
                    index.toImageTitle()
                }

                presignedUrlRepository.getProductPresignedUrls(titles)
            } ?: Result.success(emptyList())

        if (localPresignedResult.isFailure) return localPresignedResult

        val indexedLocalPresignedUrls = localImages
            .zip(localPresignedResult.getOrThrow())
            .map { (original, presigned) ->
                presigned.copy(imageName = original.first.toImageTitle())
            }

        val remotePresignedUrls = remoteImages.map { (index, url) ->
            PresignedUrl(imageName = index.toImageTitle(), url = url)
        }

        return Result.success(indexedLocalPresignedUrls + remotePresignedUrls)
    }

    private fun Int.toImageTitle(): String = "$IMAGE_TITLE_PREFIX${this + 1}"
}
