package com.napzak.market.registration.model

import android.net.Uri
import java.util.UUID

data class Photo(
    val uri: Uri,
    val uuid: String = UUID.randomUUID().toString()
)
