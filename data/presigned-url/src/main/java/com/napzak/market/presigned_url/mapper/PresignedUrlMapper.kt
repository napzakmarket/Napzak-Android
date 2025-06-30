package com.napzak.market.presigned_url.mapper

import com.napzak.market.presigned_url.dto.ChatPresignedUrlResponse
import com.napzak.market.presigned_url.dto.ProductPresignedUrlResponse
import com.napzak.market.presigned_url.dto.ProfilePresignedUrlResponse
import com.napzak.market.presigned_url.model.PresignedUrl


/**
 * To domain
 * Map 형태의 데이터를 PresignedUrl List 형태로 변환
 */
fun ProductPresignedUrlResponse.toDomain() = presignedUrls.map { (imageName, url) ->
    PresignedUrl(
        imageName = imageName,
        url = url,
    )
}

/**
 * To domain
 * Map 형태의 데이터를 PresignedUrl List 형태로 변환
 */
fun ProfilePresignedUrlResponse.toDomain() = presignedUrls.map { (imageName, url) ->
    PresignedUrl(
        imageName = imageName,
        url = url,
    )
}

/**
 * To domain
 * Map 형태의 데이터를 PresignedUrl List 형태로 변환
 */
fun ChatPresignedUrlResponse.toDomain() = presignedUrls.map { (imageName, url) ->
    PresignedUrl(
        imageName = imageName,
        url = url,
    )
}