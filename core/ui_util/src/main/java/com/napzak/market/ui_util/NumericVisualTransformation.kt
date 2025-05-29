package com.napzak.market.ui_util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat

private const val MILLION = 1_000_000

fun priceSeparatorTransformation(): VisualTransformation {
    return VisualTransformation { text ->
        val originalText = text.text
        val formattedText = originalText.toLongOrNull()?.let {
            DecimalFormat("#,###").format(it)
        } ?: originalText

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return if (offset == 0) 0 else formattedText.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                return text.length
            }
        }
        TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}

fun String.priceToNumericTransformation(): Int {
    return this.replace(",", "").toIntOrNull() ?: 0
}

fun String.addPrice(amount: Int): String =
    (this.priceToNumericTransformation() + amount).coerceAtMost(MILLION).toString()

fun TextFieldValue.adjustToMaxPrice(maxPrice: Int): TextFieldValue {
    if (this.text.isBlank()) return this

    val limitedValue = this.text.priceToNumericTransformation().coerceAtMost(maxPrice).toString()

    return if (this.text == limitedValue) this
    else {
        TextFieldValue(
            text = limitedValue,
            selection = TextRange(
                limitedValue.length,
            ),
        )
    }
}
