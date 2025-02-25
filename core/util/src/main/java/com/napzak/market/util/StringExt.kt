package com.napzak.market.util

import java.text.DecimalFormat

fun String.formatToPriceString(): String = toLongOrNull()?.let {
    DecimalFormat("#,###").format(it)
} ?: this
