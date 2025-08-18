package com.napzak.market.ui_util

import android.graphics.Color
import android.os.Build
import android.view.Window

fun Window.disableContrastEnforcement() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        isNavigationBarContrastEnforced = false
    } else {
        this.navigationBarColor = Color.TRANSPARENT
        this.navigationBarDividerColor = Color.TRANSPARENT
    }
}
