package com.napzak.market.presigned_url.usecase

import com.napzak.market.presigned_url.repository.PresignedUrlRepository
import com.napzak.market.presigned_url.type.PhotoType
import javax.inject.Inject

class UploadStorePhotoUseCase @Inject constructor(
    private val presignedUrlRepository: PresignedUrlRepository
) {

    /**
     * 이미지 타입으로 구분된 새로운 사진 url을 반환합니다.
     * 만약 수정되지 않았다면 빈 url이 포함됩니다
     *
     * @param coverPhoto 변경된 커버 사진의 URI (시스템 내 URI)
     * @param profilePhoto 변경된 프로필 사진의 URI (시스템 내 URI)
     * @return Map<PhotoType, String> 사진의 타입, Presigned URL
     */
    suspend operator fun invoke(
        coverPhoto: String?,
        profilePhoto: String?
    ): Result<Map<PhotoType, String?>> = runCatching {

        val photoMap = buildMap {
            if (coverPhoto != null) put(COVER_IMAGE_TITLE, coverPhoto)
            if (profilePhoto != null) put(PROFILE_IMAGE_TITLE, profilePhoto)
        }

        val presignedUrlMap = getPresignedUrlMap(photoMap)

        putImageOnS3(presignedUrlMap[PhotoType.COVER], coverPhoto)
        putImageOnS3(presignedUrlMap[PhotoType.PROFILE], profilePhoto)

        mapOf(
            PhotoType.COVER to presignedUrlMap[PhotoType.COVER],
            PhotoType.PROFILE to (presignedUrlMap[PhotoType.PROFILE]),
        )
    }

    /**
     * 서버로부터 이미지에 대한 presigned url을 가져옵니다.
     *
     * @return Map<PhotoType, String> 사진의 타입과 presigned url
     * @throws IllegalArgumentException 사진이 바뀌지 않았을 경우
     * @throws Throwable presigned url을 가져오지 못할 경우
     */
    private suspend fun getPresignedUrlMap(photoMap: Map<String, String>): Map<PhotoType, String> {
        val imageTitles = photoMap.map { it.key }
        val presignedUrls = presignedUrlRepository.getProfilePresignedUrls(imageTitles).getOrThrow()

        // 각 케이스별로 presigned url이 있는지 확인, 없으면 -1 반환
        val coverIndex = presignedUrls.indexOfFirst { it.imageName == COVER_IMAGE_TITLE }
        val profileIndex = presignedUrls.indexOfFirst { it.imageName == PROFILE_IMAGE_TITLE }

        return mutableMapOf<PhotoType, String>().apply {
            if (coverIndex != -1) put(PhotoType.COVER, presignedUrls[coverIndex].url)
            if (profileIndex != -1) put(PhotoType.PROFILE, presignedUrls[profileIndex].url)
        }
    }

    /**
     * 서버에서 받아온 presigned url을 통해 이미지를 업로드합니다.
     *
     * @throws IllegalArgumentException presigned url 또는 image uri가 null인 경우
     */
    private suspend fun putImageOnS3(presignedUrl: String?, imageUri: String?) {
        if (presignedUrl == null || imageUri == null) return
        presignedUrlRepository.putViaPresignedUrl(presignedUrl, imageUri).getOrThrow()
    }

    companion object {
        const val PROFILE_IMAGE_TITLE = "profile.png"
        const val COVER_IMAGE_TITLE = "cover.png"
    }
}