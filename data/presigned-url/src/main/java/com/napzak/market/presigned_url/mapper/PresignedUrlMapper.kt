package com.napzak.market.presigned_url.mapper

import com.napzak.market.presigned_url.dto.ProductPresignedUrlResponseDto
import com.napzak.market.presigned_url.dto.ProfilePresignedUrlResponseDto
import com.napzak.market.presigned_url.model.PresignedUrlMap

fun ProductPresignedUrlResponseDto.toPresignedUrlMap() = PresignedUrlMap(
    presignedUrls = LinkedHashMap(presignedUrls),
)

fun ProfilePresignedUrlResponseDto.toPresignedUrlMap() = PresignedUrlMap(
    presignedUrls = LinkedHashMap(presignedUrls),
)
