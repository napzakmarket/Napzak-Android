package com.napzak.market.registration.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.R.drawable.ic_error_9
import com.napzak.market.designsystem.component.textfield.InputTextField
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.registration.R.string.purchase_price_error
import com.napzak.market.util.android.addPrice
import com.napzak.market.util.android.priceSeparatorTransformation
import com.napzak.market.util.android.priceToNumericTransformation

private const val THOUSAND = 1000
private const val FIVE_THOUSAND = 5000
private const val TNE_THOUSAND = 10000
private const val HUNDRED_THOUSAND = 100000

@Composable
internal fun PriceSettingGroup(
    tradeType: TradeType,
    title: String,
    description: String,
    price: String,
    onPriceChange: (String) -> Unit,
    priceTag: String,
    modifier: Modifier = Modifier,
) {
    val transformedPrice = price.priceToNumericTransformation()
    val purchaseError = tradeType == TradeType.BUY && transformedPrice != 0 && transformedPrice % THOUSAND != 0
    val textColor = if (purchaseError) NapzakMarketTheme.colors.red else NapzakMarketTheme.colors.gray400
    val borderColor = if (purchaseError) NapzakMarketTheme.colors.red else NapzakMarketTheme.colors.gray100

    Column(
        modifier = modifier,
    ) {
        Text(
            text = title,
            style = NapzakMarketTheme.typography.body14b.copy(
                color = NapzakMarketTheme.colors.gray500,
            ),
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = description,
            style = NapzakMarketTheme.typography.caption12m.copy(
                color = NapzakMarketTheme.colors.gray300,
            ),
        )

        Spacer(modifier = Modifier.height(24.dp))

        InputTextField(
            text = price,
            onTextChange = onPriceChange,
            textStyle = NapzakMarketTheme.typography.body14b,
            textColor = textColor,
            borderColor = borderColor,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            ),
            visualTransformation = priceSeparatorTransformation(),
            paddingValues = PaddingValues(12.dp, 16.dp, 12.dp, 16.dp),
            contentAlignment = Alignment.CenterEnd,
            textAlign = TextAlign.End,
            suffix = {
                Text(
                    text = priceTag,
                    style = NapzakMarketTheme.typography.body14b.copy(
                        color = textColor,
                    ),
                )
            }
        )

        Spacer(modifier = Modifier.height(6.dp))

        if (tradeType == TradeType.BUY) {
            AnimatedVisibility(
                visible = transformedPrice != 0 && transformedPrice % THOUSAND != 0,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                ) {
                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(ic_error_9),
                            contentDescription = null,
                            tint = NapzakMarketTheme.colors.red,
                        )
                        Text(
                            text = stringResource(purchase_price_error),
                            style = NapzakMarketTheme.typography.caption10sb.copy(
                                color = NapzakMarketTheme.colors.red,
                            ),
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        PriceIncrementer(
            onAddOneClick = { onPriceChange(price.addPrice(THOUSAND)) },
            onAddFiveClick = { onPriceChange(price.addPrice(FIVE_THOUSAND)) },
            onAddTenClick = { onPriceChange(price.addPrice(TNE_THOUSAND)) },
            onAddHundredClick = { onPriceChange(price.addPrice(HUNDRED_THOUSAND)) },
        )
    }
}

@Preview
@Composable
private fun PriceSettingGroupPreview() {
    NapzakMarketTheme {
        var price by remember { mutableStateOf("") }

        PriceSettingGroup(
            tradeType = TradeType.SELL,
            title = "가격",
            description = "얼마에 거래하고 싶으신가요? (최대 100만원)",
            price = price,
            onPriceChange = { price = it },
            priceTag = "원대",
        )
    }
}
