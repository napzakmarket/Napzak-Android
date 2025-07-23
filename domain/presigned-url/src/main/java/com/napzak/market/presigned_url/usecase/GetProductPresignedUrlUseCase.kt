package com.napzak.market.presigned_url.usecase

import com.napzak.market.presigned_url.model.PresignedUrl
import com.napzak.market.presigned_url.repository.PresignedUrlRepository
import javax.inject.Inject

class GetProductPresignedUrlUseCase @Inject constructor(
    private val presignedUrlRepository: PresignedUrlRepository,
) {
    suspend operator fun invoke(
        images: List<Pair<String, Int>>,
    ): Result<List<PresignedUrl>> {
        val (remoteImages, localImages) = images.partition { (uri, _) ->
            uri.startsWith(REMOTE_URL_KEY)
        }

        val localPresignedResult = localImages
            .takeIf { it.isNotEmpty() }
            ?.let { images ->
                val titles = images.map { (_, index) ->
                    index.toImageTitle()
                }

                presignedUrlRepository.getProductPresignedUrls(titles)
            } ?: Result.success(emptyList())

        if (localPresignedResult.isFailure) return localPresignedResult

        val indexedLocalPresignedUrls = localImages
            .zip(localPresignedResult.getOrThrow())
            .map { (original, presigned) ->
                presigned.copy(imageName = original.second.toImageTitle())
            }

        val remotePresignedUrls = remoteImages.map { (url, index) ->
            PresignedUrl(imageName = index.toImageTitle(), url = url)
        }

        return Result.success(indexedLocalPresignedUrls + remotePresignedUrls)
    }

    private fun Int.toImageTitle(): String = "$IMAGE_TITLE_PREFIX${this + 1}"

    companion object {
        private const val IMAGE_TITLE_PREFIX = "image_"
        private const val REMOTE_URL_KEY = "https://"
    }
}
