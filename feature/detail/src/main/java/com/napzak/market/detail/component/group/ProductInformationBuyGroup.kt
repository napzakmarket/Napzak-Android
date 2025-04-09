package com.napzak.market.detail.component.group

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.common.type.ProductConditionType
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.detail.component.divider.SectionDivider
import com.napzak.market.feature.detail.R.string.detail_product_delivery_half
import com.napzak.market.feature.detail.R.string.detail_product_delivery_included
import com.napzak.market.feature.detail.R.string.detail_product_delivery_normal
import com.napzak.market.feature.detail.R.string.detail_product_title_product_condition


@Composable
internal fun ProductInformationBuyGroup(
    productCondition: ProductConditionType,
    isDeliveryIncluded: Boolean,
    standardDeliveryFee: Int,
    halfDeliveryFee: Int,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        ProductConditionSection(
            productCondition = productCondition,
        )
        SectionDivider(Modifier.padding(vertical = 26.dp))
        ProductDeliverySection(
            isDeliveryIncluded = isDeliveryIncluded,
            standardDeliveryFee = standardDeliveryFee,
            halfDeliveryFee = halfDeliveryFee,
        )
    }
}

@Composable
private fun ProductConditionSection(
    productCondition: ProductConditionType,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Text(
            text = stringResource(detail_product_title_product_condition),
            style = NapzakMarketTheme.typography.body14b.copy(
                color = NapzakMarketTheme.colors.gray500
            ),
        )

        Text(
            text = productCondition.label,
            style = NapzakMarketTheme.typography.body14b.copy(
                color = NapzakMarketTheme.colors.gray300
            ),
            modifier = Modifier
                .background(
                    color = NapzakMarketTheme.colors.gray50,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(vertical = 6.dp, horizontal = 16.dp)

        )
    }
}

@Composable
private fun ProductDeliverySection(
    isDeliveryIncluded: Boolean,
    standardDeliveryFee: Int,
    halfDeliveryFee: Int,
    modifier: Modifier = Modifier,
) {
    val deliveryTypes = mutableListOf<String>().apply {
        if (isDeliveryIncluded) {
            add(stringResource(detail_product_delivery_included))
        }
        if (halfDeliveryFee != 0) {
            add(stringResource(detail_product_delivery_half, halfDeliveryFee))
        }
        if (standardDeliveryFee != 0) {
            add(stringResource(detail_product_delivery_normal, standardDeliveryFee))
        }
    }.toList()

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Text(
            text = stringResource(detail_product_title_product_condition),
            style = NapzakMarketTheme.typography.body14b.copy(
                color = NapzakMarketTheme.colors.gray500
            ),
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.End,
        ) {
            deliveryTypes.forEach { type ->
                Text(
                    text = type,
                    style = NapzakMarketTheme.typography.body14b.copy(
                        color = NapzakMarketTheme.colors.gray300
                    ),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductInformationBuyGroupPreview() {
    NapzakMarketTheme {
        ProductInformationBuyGroup(
            productCondition = ProductConditionType.NEW,
            isDeliveryIncluded = true,
            standardDeliveryFee = 2500,
            halfDeliveryFee = 0,
        )
    }
}
