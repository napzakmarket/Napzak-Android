package com.napzak.market.ui_util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
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

fun String.underline(target: String): AnnotatedString {
    val fullText = this
    val startIndex = fullText.indexOf(target)

    if (startIndex == -1) return AnnotatedString(this)

    val endIndex = startIndex + target.length

    return buildAnnotatedString {
        append(fullText)
        addStyle(
            style = SpanStyle(textDecoration = TextDecoration.Underline),
            start = startIndex,
            end = endIndex
        )
    }
}