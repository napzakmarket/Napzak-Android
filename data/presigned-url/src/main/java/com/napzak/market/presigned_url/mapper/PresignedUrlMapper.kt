package com.napzak.market.presigned_url.mapper

import com.napzak.market.presigned_url.dto.ProductPresignedUrlResponse
import com.napzak.market.presigned_url.dto.ProfilePresignedUrlResponse
import com.napzak.market.presigned_url.model.PresignedUrlMap

fun ProductPresignedUrlResponse.toPresignedUrlMap() = PresignedUrlMap(
    presignedUrls = LinkedHashMap(presignedUrls),
)

fun ProfilePresignedUrlResponse.toPresignedUrlMap() = PresignedUrlMap(
    presignedUrls = LinkedHashMap(presignedUrls),
)
