package com.napzak.market.util.common

import java.text.DecimalFormat

fun String.formatToPriceString(): String = toLongOrNull()?.let {
    DecimalFormat("#,###").format(it)
} ?: this
