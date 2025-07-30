package com.napzak.market.registration.model

import android.net.Uri
import java.util.UUID

data class Photo(
    val uri: Uri,
    val uuid: String = UUID.randomUUID().toString(),
    val photoId: Long? = null,
    val compressedUri: Uri? = null,
    val status: PhotoStatus = PhotoStatus.COMPRESSING,
) {
    enum class PhotoStatus {
        COMPRESSING,
        SUCCESS,
        ERROR;
    }
}
