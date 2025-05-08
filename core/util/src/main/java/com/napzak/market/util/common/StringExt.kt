package com.napzak.market.util.common

import java.text.DecimalFormat

fun String.formatToPriceString(): String = toLongOrNull()?.let {
    DecimalFormat("#,###").format(it)
} ?: this

fun String.ellipsis(maxLength: Int): String {
    return if (this.length > maxLength) {
        this.take(maxLength) + "…"
    } else {
        this
    }
}