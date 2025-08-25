package com.napzak.market.presigned_url.repository

import androidx.core.net.toUri
import com.napzak.market.presigned_url.ImageCompressor
import com.napzak.market.presigned_url.datasource.PresignedUrlDataSource
import com.napzak.market.presigned_url.mapper.toDomain
import com.napzak.market.presigned_url.model.PresignedUrl
import com.napzak.market.util.android.suspendRunCatching
import javax.inject.Inject

class PresignedUrlRepositoryImpl @Inject constructor(
    private val presignedUrlDataSource: PresignedUrlDataSource,
    private val imageCompressor: ImageCompressor,
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

    override suspend fun compressProductImage(
        imageUri: String,
    ): Result<String> = runCatching {
        imageCompressor.compressImage(imageUri.toUri())
    }.mapCatching { uri ->
        uri.toString()
    }

    override suspend fun clearCachedImage(): Result<Unit> = runCatching {
        imageCompressor.clearCachedImage()
    }
}
