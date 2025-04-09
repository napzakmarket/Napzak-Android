package com.napzak.market.detail.component.group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.detail.R.string.detail_product_is_price_negotiable_false
import com.napzak.market.feature.detail.R.string.detail_product_is_price_negotiable_true
import com.napzak.market.feature.detail.R.string.detail_product_title_is_price_negotiable

@Composable
internal fun ProductInformationSellGroup(
    isPriceNegotiable: Boolean,
    modifier: Modifier = Modifier,
) {
    val negotiableStringRes = if (isPriceNegotiable) detail_product_is_price_negotiable_true
    else detail_product_is_price_negotiable_false

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Text(
            text = stringResource(detail_product_title_is_price_negotiable),
            style = NapzakMarketTheme.typography.body14b.copy(
                color = NapzakMarketTheme.colors.gray500
            ),
        )

        Text(
            text = stringResource(negotiableStringRes),
            style = NapzakMarketTheme.typography.body14b.copy(
                color = NapzakMarketTheme.colors.gray300
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductInformationSellGroupPreview() {
    NapzakMarketTheme {
        ProductInformationSellGroup(
            isPriceNegotiable = true,
        )
    }
}