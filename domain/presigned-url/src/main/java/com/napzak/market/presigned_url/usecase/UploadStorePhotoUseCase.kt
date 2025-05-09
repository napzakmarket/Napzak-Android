package com.napzak.market.presigned_url.usecase

import com.napzak.market.presigned_url.model.PresignedUrl
import com.napzak.market.presigned_url.repository.PresignedUrlRepository
import javax.inject.Inject

class UploadStorePhotoUseCase @Inject constructor(
    private val presignedUrlRepository: PresignedUrlRepository
) {
    suspend operator fun invoke(
        coverPhoto: String?,
        profilePhoto: String?
    ): Result<Map<String, String>> = runCatching {
        val photoList = mutableListOf<Pair<String, String>>().apply {
            if (coverPhoto != null) add(COVER_IMAGE_TITLE to coverPhoto)
            if (profilePhoto != null) add(PROFILE_IMAGE_TITLE to profilePhoto)
            if (isEmpty()) throw Throwable(MESSAGE_URL_NOT_VALID)
        }

        val presignedUrlMap = getProfilePresignedUrls(photoList)
        if (presignedUrlMap != null) {
            uploadImageOnS3(presignedUrlMap[COVER_IMAGE_TITLE], coverPhoto)
            uploadImageOnS3(presignedUrlMap[PROFILE_IMAGE_TITLE], profilePhoto)
        }

        mapOf(
            PROFILE_IMAGE_TITLE to (presignedUrlMap?.get(PROFILE_IMAGE_TITLE)
                ?: EMPTY_PRESIGNED_URL),
            COVER_IMAGE_TITLE to (presignedUrlMap?.get(COVER_IMAGE_TITLE) ?: EMPTY_PRESIGNED_URL)
        )
    }

    private suspend fun getProfilePresignedUrls(photos: List<Pair<String, String>>): Map<String, String>? {
        val presignedUrl: PresignedUrl? =
            presignedUrlRepository.getProfilePresignedUrls(photos.map { it.first }).getOrNull()
        val s3Map = presignedUrl?.let { extractS3Url(presignedUrl) }
        return s3Map
    }

    private fun extractS3Url(presignedUrl: PresignedUrl): Map<String, String> {
        val coverIndex = presignedUrl.imageNames.indexOf(COVER_IMAGE_TITLE)
        val profileIndex = presignedUrl.imageNames.indexOf(PROFILE_IMAGE_TITLE)

        val s3Map = mutableMapOf<String, String>().apply {
            if (coverIndex != -1) put(COVER_IMAGE_TITLE, presignedUrl.urls[coverIndex])
            if (profileIndex != -1) put(PROFILE_IMAGE_TITLE, presignedUrl.urls[profileIndex])
        }

        return s3Map
    }

    private suspend fun uploadImageOnS3(presignedUrl: String?, imageUri: String?): Boolean {
        if (presignedUrl == null || imageUri == null) return false
        presignedUrlRepository.putViaPresignedUrl(presignedUrl, imageUri)
            .fold(
                onSuccess = { return true },
                onFailure = { throw it }
            )
    }

    companion object {
        private const val MESSAGE_URL_NOT_VALID = "Presigned URL or image URI is null"

        const val EMPTY_PRESIGNED_URL = ""
        const val PROFILE_IMAGE_TITLE = "profile.png"
        const val COVER_IMAGE_TITLE = "cover.png"
    }
}