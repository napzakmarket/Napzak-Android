package com.napzak.market.presigned_url.usecase

import com.napzak.market.presigned_url.model.PresignedUrl
import com.napzak.market.presigned_url.repository.PresignedUrlRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val presignedUrlRepository: PresignedUrlRepository,
) {
    suspend operator fun invoke(
        presignedUrls: List<PresignedUrl>,
        images: List<Pair<Int, String>>,
    ): Result<List<PresignedUrl>> = coroutineScope {
        val (_, localImages) = images.partition { (_, uri) ->
            uri.startsWith(REMOTE_URL_KEY)
        }

        val presignedMap = presignedUrls.associateBy { it.imageName }

        val localUploadResults = localImages.map { (index, uri) ->
            val imageTitle = index.toImageTitle()
            val presignedUrl = presignedMap[imageTitle]
                ?: return@coroutineScope Result.failure(IllegalArgumentException("$MISSING_URL: $imageTitle"))

            async {
                presignedUrlRepository.putViaPresignedUrl(presignedUrl.url, uri)
            }
        }.awaitAll()

        localUploadResults.firstOrNull { it.isFailure }?.exceptionOrNull()?.let { e ->
            return@coroutineScope Result.failure(e)
        }

        val resultList = images.map { (index, uri) ->
            val imageTitle = index.toImageTitle()

            if (uri.startsWith(REMOTE_URL_KEY))
                PresignedUrl(imageName = imageTitle, url = uri)
            else presignedMap[imageTitle]
                ?: return@coroutineScope Result.failure(IllegalStateException("$MISSING_URL: $imageTitle"))
        }

        Result.success(resultList)
    }

    private fun Int.toImageTitle(): String = "$IMAGE_TITLE_PREFIX${this + 1}"

    companion object {
        internal const val IMAGE_TITLE_PREFIX = "image_"
        internal const val REMOTE_URL_KEY = "https://"
        internal const val MISSING_URL = "missing presigned URL"
    }
}
