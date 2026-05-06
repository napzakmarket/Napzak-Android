package com.napzak.market.onboarding.phoneVerification.util

internal fun String.formatPhoneNumber(): String {
    val digits = this.filter { it.isDigit() }

    return when {
        digits.length <= 3 -> digits
        digits.length <= 7 -> {
            "${digits.take(3)}-${digits.substring(3)}"
        }
        digits.length == 10 -> {
            if (digits.take(2) == "02") "${digits.take(2)}-${digits.substring(2, 6)}-${digits.substring(6)}"
            else "${digits.take(3)}-${digits.substring(3, 7)}-${digits.substring(7)}"
        }
        digits.length <= 11 -> {
            "${digits.take(3)}-${digits.substring(3, 7)}-${digits.substring(7)}"
        }
        else -> {
            "${digits.take(3)}-${digits.substring(3, 7)}-${digits.substring(7, 11)}"
        }
    }
}
