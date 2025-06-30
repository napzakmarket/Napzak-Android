package com.napzak.market.presigned_url.repository

import com.napzak.market.presigned_url.model.PresignedUrl

interface PresignedUrlRepository {
    suspend fun getProductPresignedUrls(imageTitles: List<String>): Result<List<PresignedUrl>>

    suspend fun getProfilePresignedUrls(imageTitles: List<String>): Result<List<PresignedUrl>>

    suspend fun getChatPresignedUrls(imageTitles: List<String>): Result<List<PresignedUrl>>

    suspend fun putViaPresignedUrl(presignedUrl: String, imageUri: String): Result<Unit>
}
