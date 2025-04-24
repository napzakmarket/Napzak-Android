package com.napzak.market.util.common

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openUrl(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}