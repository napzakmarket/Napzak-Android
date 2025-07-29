package com.napzak.market.ui_util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openUrl(url: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    } catch (_: Exception) {
    }
}