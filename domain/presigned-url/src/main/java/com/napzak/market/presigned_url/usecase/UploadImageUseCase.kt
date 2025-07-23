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
        val (remoteImages, localImages) = images.partition { (_, uri) ->
            uri.startsWith(REMOTE_URL_KEY)
        }

        val localPresignedUrls = presignedUrls.zip(localImages) { presignedUrl, (_, uri) ->
            presignedUrl to uri
        }

        val uploadResults = localPresignedUrls.map { (presignedUrl, photoUri) ->
            async {
                presignedUrlRepository.putViaPresignedUrl(presignedUrl.url, photoUri)
            }
        }.awaitAll()

        if (uploadResults.all { it.isSuccess }) {
            val remotePresigned = remoteImages.map { (index, uri) ->
                PresignedUrl(imageName = index.toImageTitle(), url = uri)
            }

            Result.success(presignedUrls + remotePresigned)
        } else {
            val failureResult = uploadResults.first { it.isFailure }

            Result.failure(failureResult.exceptionOrNull() ?: Exception(UNKNOWN_ERROR))
        }
    }

    private fun Int.toImageTitle(): String = "$IMAGE_TITLE_PREFIX${this + 1}"

    companion object {
        internal const val IMAGE_TITLE_PREFIX = "image_"
        internal const val REMOTE_URL_KEY = "https://"
        internal const val UNKNOWN_ERROR = "unknown error."
    }
}
