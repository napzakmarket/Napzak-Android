package com.napzak.market.registration.sale.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.registration.R.string.sale_price_tag
import com.napzak.market.ui_util.EmptyTextToolbar
import com.napzak.market.ui_util.adjustToMaxPrice
import com.napzak.market.ui_util.priceSeparatorTransformation

@Composable
fun ShippingFeeTextField(
    price: String,
    onPriceChange: (String) -> Unit,
    hint: String,
    maxPrice: Int,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    var priceFieldValue by remember { mutableStateOf(TextFieldValue(price)) }

    LaunchedEffect(price) {
        if (price != priceFieldValue.text) {
            priceFieldValue = TextFieldValue(
                text = price,
                selection = TextRange(price.length)
            )
        }
    }

    CompositionLocalProvider(
        LocalTextToolbar provides EmptyTextToolbar()
    ) {
        BasicTextField(
            value = priceFieldValue,
            onValueChange = {
                val limited = it.adjustToMaxPrice(maxPrice)
                priceFieldValue = limited
                onPriceChange(limited.text)
            },
            modifier = modifier,
            enabled = enabled,
            textStyle = NapzakMarketTheme.typography.body14sb.copy(
                color = NapzakMarketTheme.colors.gray400,
                textAlign = TextAlign.End,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
            ),
            singleLine = true,
            visualTransformation = priceSeparatorTransformation(),
            cursorBrush = SolidColor(NapzakMarketTheme.colors.gray400),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier,
                    ) {
                        if (price.isEmpty()) {
                            Text(
                                text = hint,
                                style = NapzakMarketTheme.typography.body14sb.copy(
                                    color = NapzakMarketTheme.colors.gray100,
                                ),
                                modifier = Modifier.align(Alignment.CenterEnd),
                            )
                        }
                        innerTextField()
                    }

                    Text(
                        text = stringResource(sale_price_tag),
                        style = NapzakMarketTheme.typography.body14sb.copy(
                            color = if (price.isEmpty()) NapzakMarketTheme.colors.gray100 else NapzakMarketTheme.colors.gray400,
                        ),
                    )
                }
            },
        )
    }
}

@Preview
@Composable
private fun ShippingFeeTextFieldPreview() {
    NapzakMarketTheme {
        var price by remember { mutableStateOf("") }

        ShippingFeeTextField(
            price = price,
            onPriceChange = { price = it },
            hint = "100~30,000",
            maxPrice = 30000,
            enabled = true,
        )
    }
}
