package com.napzak.market.presigned_url.repository

import com.napzak.market.presigned_url.datasource.PresignedUrlDataSource
import com.napzak.market.presigned_url.mapper.toDomain
import com.napzak.market.presigned_url.model.PresignedUrl
import com.napzak.market.util.android.suspendRunCatching
import javax.inject.Inject

class PresignedUrlRepositoryImpl @Inject constructor(
    private val presignedUrlDataSource: PresignedUrlDataSource,
) : PresignedUrlRepository {
    override suspend fun getProductPresignedUrls(
        imageTitles: List<String>,
    ): Result<List<PresignedUrl>> = runCatching {
        presignedUrlDataSource.getProductPresignedUrl(imageTitles = imageTitles).toDomain()
    }

    override suspend fun getProfilePresignedUrls(
        imageTitles: List<String>,
    ): Result<List<PresignedUrl>> = runCatching {
        presignedUrlDataSource.getProfilePresignedUrl(imageTitles = imageTitles).toDomain()
    }

    override suspend fun getChatPresignedUrls(
        imageTitles: List<String>,
    ): Result<List<PresignedUrl>> = suspendRunCatching {
        presignedUrlDataSource.getChatPresignedUrl(imageTitles).toDomain()
    }

    override suspend fun putViaPresignedUrl(
        presignedUrl: String,
        imageUri: String,
    ): Result<Unit> = runCatching {
        presignedUrlDataSource.putViaPresignedUrl(presignedUrl, imageUri)
    }
}
