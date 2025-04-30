package com.napzak.market.presigned_url.datasource

import android.content.ContentResolver
import androidx.core.net.toUri
import com.napzak.market.presigned_url.service.PresignedUrlService
import com.napzak.market.util.android.ContentUriRequestBody
import javax.inject.Inject

class PresignedUrlDataSource @Inject constructor(
    private val contentResolver: ContentResolver,
    private val presignedUrlService: PresignedUrlService,
) {
    suspend fun getProductPresignedUrl(
        imageTitles: List<String>,
    ) = presignedUrlService.getProductPresignedUrl(imageTitles).data

    suspend fun getProfilePresignedUrl(
        imageTitles: List<String>,
    ) = presignedUrlService.getProfilePresignedUrl(imageTitles).data

    suspend fun putViaPresignedUrl(
        presignedUrl: String,
        imageUri: String,
    ) = presignedUrlService
        .putViaPresignedUrl(presignedUrl, ContentUriRequestBody(contentResolver, imageUri.toUri()))
}
