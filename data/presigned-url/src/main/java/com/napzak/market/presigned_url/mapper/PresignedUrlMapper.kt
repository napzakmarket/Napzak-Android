package com.napzak.market.presigned_url.mapper

import com.napzak.market.presigned_url.dto.ProductPresignedUrlResponse
import com.napzak.market.presigned_url.dto.ProfilePresignedUrlResponse
import com.napzak.market.presigned_url.model.PresignedUrl


/**
 * To domain
 * Map 형태의 데이터를 두 개의 List로 변환(imageNames, urls)
 */
fun ProductPresignedUrlResponse.toDomain() = PresignedUrl(
    imageNames = presignedUrls.keys.toList(),
    urls = presignedUrls.values.toList(),
)

/**
 * To domain
 * Map 형태의 데이터를 두 개의 List로 변환(imageNames, urls)
 */
fun ProfilePresignedUrlResponse.toDomain() = PresignedUrl(
    imageNames = presignedUrls.keys.toList(),
    urls = presignedUrls.values.toList(),
)
