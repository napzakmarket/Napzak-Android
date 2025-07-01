package com.napzak.market.presigned_url.usecase

import com.napzak.market.presigned_url.model.UploadImage
import com.napzak.market.presigned_url.repository.PresignedUrlRepository
import com.napzak.market.util.android.suspendRunCatching
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import javax.inject.Inject

class UploadImagesUseCase @Inject constructor(
    private val presignedUrlRepository: PresignedUrlRepository
) {
    suspend operator fun invoke(
        images: List<UploadImage>
    ): Result<Map<UploadImage.ImageType, String>> = suspendRunCatching {
        val uploadImages = getPresignedUrls(images)
        putImageOnS3(uploadImages)

        buildMap {
            uploadImages.forEach { image ->
                put(image.imageType, image.presignedUrl)
            }
        }
    }

    private suspend fun getPresignedUrls(images: List<UploadImage>): List<UploadImage> {
        val imageTitles = images.map { imageType -> imageType.title }
        val presignedUrls = presignedUrlRepository.getProfilePresignedUrls(imageTitles).getOrThrow()
        return presignedUrls.zip(images).map { (presignedUrl, image) ->
            image.copy(presignedUrl = presignedUrl.url)
        }
    }

    private suspend fun putImageOnS3(uploadImages: List<UploadImage>) = coroutineScope {
        val semaphore = Semaphore(MAX_CONCURRENT_REQUESTS)
        uploadImages.map { image ->
            async {
                semaphore.withPermit {
                    presignedUrlRepository.putViaPresignedUrl(image.presignedUrl, image.uri)
                }.getOrThrow()
            }
        }.awaitAll()
    }

    companion object {
        private const val MAX_CONCURRENT_REQUESTS = 10
    }
}