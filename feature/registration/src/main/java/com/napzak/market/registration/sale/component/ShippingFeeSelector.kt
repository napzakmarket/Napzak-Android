package com.napzak.market.registration.sale.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_checked_box
import com.napzak.market.designsystem.R.drawable.ic_unchecked_box
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.registration.R.string.half_priced_shipping
import com.napzak.market.feature.registration.R.string.half_priced_shipping_hint
import com.napzak.market.feature.registration.R.string.normal_shipping
import com.napzak.market.feature.registration.R.string.normal_shipping_hint
import com.napzak.market.feature.registration.R.string.shipping_excluded
import com.napzak.market.feature.registration.R.string.shipping_included
import com.napzak.market.ui_util.bringContentIntoView
import com.napzak.market.ui_util.noRippleClickable

private const val EMPTY_STRING = ""
private const val NORMAL_MAX_PRICE = 30_000
private const val HALF_MAX_PRICE = 5_000

@Composable
internal fun ShippingFeeSelector(
    isShippingIncluded: Boolean?,
    onShippingFeeSelect: (Boolean) -> Unit,
    isNormalShippingChecked: Boolean,
    onNormalShippingSelect: (Boolean) -> Unit,
    normalShippingFee: String,
    onNormalShippingFeeChange: (String) -> Unit,
    isHalfShippingChecked: Boolean,
    onHalfShippingSelect: (Boolean) -> Unit,
    halfShippingFee: String,
    onHalfShippingFeeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val resetShippingExcluded = remember {
        {
            onNormalShippingSelect(false)
            onHalfShippingSelect(false)
            onNormalShippingFeeChange(EMPTY_STRING)
            onHalfShippingFeeChange(EMPTY_STRING)
        }
    }

    Column(
        modifier = modifier,
    ) {
        SelectorButton(
            title = stringResource(shipping_included),
            isChecked = isShippingIncluded == true,
            onCheckChange = {
                if (isShippingIncluded != true) {
                    onShippingFeeSelect(true)
                    resetShippingExcluded()
                }
            },
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(NapzakMarketTheme.colors.white)
                .border(
                    width = 1.dp,
                    color = NapzakMarketTheme.colors.gray50,
                    shape = RoundedCornerShape(14.dp),
                )
                .bringContentIntoView(),
        ) {
            Column {
                SelectorButton(
                    title = stringResource(shipping_excluded),
                    isChecked = isShippingIncluded == false,
                    onCheckChange = {
                        if (isShippingIncluded != false) {
                            onShippingFeeSelect(false)
                        }
                    },
                )

                AnimatedVisibility(
                    visible = isShippingIncluded == false,
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        ExpandedShippingFee(
                            title = stringResource(normal_shipping),
                            isChecked = isNormalShippingChecked,
                            onCheckChange = onNormalShippingSelect,
                            shippingFee = normalShippingFee,
                            onShippingFeeChange = onNormalShippingFeeChange,
                            maxPrice = NORMAL_MAX_PRICE,
                            hint = stringResource(normal_shipping_hint),
                        )

                        ExpandedShippingFee(
                            title = stringResource(half_priced_shipping),
                            isChecked = isHalfShippingChecked,
                            onCheckChange = onHalfShippingSelect,
                            shippingFee = halfShippingFee,
                            onShippingFeeChange = onHalfShippingFeeChange,
                            maxPrice = HALF_MAX_PRICE,
                            hint = stringResource(half_priced_shipping_hint),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectorButton(
    title: String,
    isChecked: Boolean,
    onCheckChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val checkIcon = if (isChecked) ic_checked_box else ic_unchecked_box

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = NapzakMarketTheme.colors.gray50,
                shape = RoundedCornerShape(14.dp),
            )
            .padding(horizontal = 10.dp, vertical = 12.dp)
            .noRippleClickable { onCheckChange(isChecked) },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(checkIcon),
            contentDescription = null,
            tint = Color.Unspecified,
        )

        Text(
            text = title,
            style = NapzakMarketTheme.typography.body14b.copy(
                color = NapzakMarketTheme.colors.gray400,
            ),
        )
    }
}

@Composable
private fun ExpandedShippingFee(
    title: String,
    isChecked: Boolean,
    onCheckChange: (Boolean) -> Unit,
    shippingFee: String,
    onShippingFeeChange: (String) -> Unit,
    maxPrice: Int,
    hint: String,
    modifier: Modifier = Modifier,
) {
    val checkIcon = if (isChecked) ic_checked_box else ic_unchecked_box
    val focusRequester = remember { FocusRequester() }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(checkIcon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.noRippleClickable {
                    onCheckChange(!isChecked)
                    if (isChecked) onShippingFeeChange(EMPTY_STRING)
                },
            )

            Text(
                text = title,
                style = NapzakMarketTheme.typography.body14r.copy(
                    color = NapzakMarketTheme.colors.gray400,
                ),
            )
        }

        ShippingFeeTextField(
            price = shippingFee,
            onPriceChange = onShippingFeeChange,
            hint = hint,
            maxPrice = maxPrice,
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged { state ->
                    if (state.isFocused) {
                        onCheckChange(true)
                    }
                },
        )
    }
}

@Preview
@Composable
private fun ShippingFeeSelectorPreview() {
    NapzakMarketTheme {
        var normalShippingFee by remember { mutableStateOf("") }
        var halfShippingFee by remember { mutableStateOf("") }

        ShippingFeeSelector(
            isShippingIncluded = true,
            onShippingFeeSelect = { },
            isNormalShippingChecked = true,
            onNormalShippingSelect = {},
            normalShippingFee = normalShippingFee,
            onNormalShippingFeeChange = { normalShippingFee = it },
            isHalfShippingChecked = true,
            onHalfShippingSelect = {},
            halfShippingFee = halfShippingFee,
            onHalfShippingFeeChange = { halfShippingFee = it },
            modifier = Modifier,
        )
    }
}
