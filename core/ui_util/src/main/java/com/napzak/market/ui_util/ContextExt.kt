package com.napzak.market.ui_util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

fun Context.openUrl(url: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    } catch (_: Exception) {
    }
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

fun Context.getVersionName(): String? = runCatching {
    packageManager.getPackageInfo(packageName, 0).versionName
}.getOrNull()
