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
import com.napzak.market.registration.component.ShippingFeeTextField
import com.napzak.market.util.android.noRippleClickable

@Composable
internal fun ShippingFeeSelector(
    isShippingFeeIncluded: Boolean,
    onShippingFeeIncludedChange: (Boolean) -> Unit,
    isShippingFeeExcluded: Boolean,
    onShippingFeeExcludedChange: (Boolean) -> Unit,
    isNormalShippingChecked: Boolean,
    onNormalShippingCheckChange: (Boolean) -> Unit,
    normalShippingFee: String,
    onNormalShippingFeeChange: (String) -> Unit,
    isHalfShippingChecked: Boolean,
    onHalfShippingCheckChange: (Boolean) -> Unit,
    halfShippingFee: String,
    onHalfShippingFeeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        SelectorButton(
            title = stringResource(shipping_included),
            isChecked = isShippingFeeIncluded,
            onCheckChange = onShippingFeeIncludedChange,
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
        ) {
            Column {
                SelectorButton(
                    title = stringResource(shipping_excluded),
                    isChecked = isShippingFeeExcluded,
                    onCheckChange = onShippingFeeExcludedChange,
                )

                AnimatedVisibility(
                    visible = isShippingFeeExcluded,
                ) {
                    Column {
                        ExpandedShippingFee(
                            title = stringResource(normal_shipping),
                            isChecked = isNormalShippingChecked,
                            onCheckChange = onNormalShippingCheckChange,
                            shippingFee = normalShippingFee,
                            onShippingFeeChange = onNormalShippingFeeChange,
                            hint = stringResource(normal_shipping_hint),
                        )
                        ExpandedShippingFee(
                            title = stringResource(half_priced_shipping),
                            isChecked = isHalfShippingChecked,
                            onCheckChange = onHalfShippingCheckChange,
                            shippingFee = halfShippingFee,
                            onShippingFeeChange = onHalfShippingFeeChange,
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

    Column(
        modifier = modifier
            .background(
                color = NapzakMarketTheme.colors.gray50,
                shape = RoundedCornerShape(14.dp),
            ),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(checkIcon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.noRippleClickable {
                    onCheckChange(isChecked)
                },
            )
            Text(
                text = title,
                style = NapzakMarketTheme.typography.body14b.copy(
                    color = NapzakMarketTheme.colors.gray400,
                ),
            )
        }
    }
}

@Composable
fun ExpandedShippingFee(
    title: String,
    isChecked: Boolean,
    onCheckChange: (Boolean) -> Unit,
    shippingFee: String,
    onShippingFeeChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
) {
    val checkIcon = if (isChecked) ic_checked_box else ic_unchecked_box

    Row(
        modifier = modifier
            .padding(20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(checkIcon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.noRippleClickable {
                    onCheckChange(isChecked)
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
        )
    }
}

@Preview
@Composable
private fun ShippingFeeSelectorPreview() {
    NapzakMarketTheme {
        var feeIncluded by remember { mutableStateOf(false) }
        var feeExcluded by remember { mutableStateOf(false) }
        var normalShippingChecked by remember { mutableStateOf(false) }
        var normalShippingFee by remember { mutableStateOf("") }
        var halfShippingChecked by remember { mutableStateOf(false) }
        var halfShippingFee by remember { mutableStateOf("") }

        ShippingFeeSelector(
            isShippingFeeIncluded = feeIncluded,
            onShippingFeeIncludedChange = { feeIncluded = !feeIncluded },
            isShippingFeeExcluded = feeExcluded,
            onShippingFeeExcludedChange = { feeExcluded = !feeExcluded },
            isNormalShippingChecked = normalShippingChecked,
            onNormalShippingCheckChange = { normalShippingChecked = !normalShippingChecked },
            normalShippingFee = normalShippingFee,
            onNormalShippingFeeChange = { normalShippingFee = it },
            isHalfShippingChecked = halfShippingChecked,
            onHalfShippingCheckChange = { halfShippingChecked = !halfShippingChecked },
            halfShippingFee = halfShippingFee,
            onHalfShippingFeeChange = { halfShippingFee = it },
            modifier = Modifier,
        )
    }
}
