package com.napzak.market.presigned_url.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfilePresignedUrlResponseDto(
    @SerialName("profilePresignedUrls")
    val presignedUrls: Map<String, String>,
)
