package com.napzak.market.presigned_url.repository

interface PresignedUrlRepository {
    suspend fun getProductPresignedUrls(imageTitles: List<String>): Result<Map<String, String>>

    suspend fun getProfilePresignedUrls(imageTitles: List<String>): Result<Map<String, String>>

    suspend fun putViaPresignedUrl(presignedUrl: String, imageUri: String): Result<Unit>
}
