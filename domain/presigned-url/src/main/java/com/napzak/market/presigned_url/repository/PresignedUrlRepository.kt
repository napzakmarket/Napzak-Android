package com.napzak.market.presigned_url.repository

import com.napzak.market.presigned_url.model.PresignedUrl

interface PresignedUrlRepository {
    suspend fun getProductPresignedUrls(imageTitles: List<String>): Result<PresignedUrl>

    suspend fun getProfilePresignedUrls(imageTitles: List<String>): Result<PresignedUrl>

    suspend fun putViaPresignedUrl(presignedUrl: String, imageUri: String): Result<Unit>
}
