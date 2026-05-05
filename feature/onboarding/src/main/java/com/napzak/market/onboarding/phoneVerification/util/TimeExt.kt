package com.napzak.market.onboarding.phoneVerification.util

import java.util.Locale

internal fun Int.toTimeFormat(): String {
    val minutes = this / 60
    val seconds = this % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}
