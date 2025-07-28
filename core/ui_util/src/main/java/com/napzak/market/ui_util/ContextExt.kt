package com.napzak.market.ui_util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

fun Context.openUrl(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}

fun Context.openSystemNotificationSettings() {
    val intent = Intent().apply {
        action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
        putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        putExtra("app_package", packageName)
        putExtra("app_uid", applicationInfo.uid)
    }
    startActivity(intent)
}
