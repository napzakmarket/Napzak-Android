package com.napzak.market.registration.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.registration.R.string.price_button_1000
import com.napzak.market.feature.registration.R.string.price_button_10000
import com.napzak.market.feature.registration.R.string.price_button_100000
import com.napzak.market.feature.registration.R.string.price_button_5000
import com.napzak.market.util.android.noRippleClickable

@Composable
internal fun PriceIncrementer(
    onAddOneClick: () -> Unit,
    onAddFiveClick: () -> Unit,
    onAddTenClick: () -> Unit,
    onAddHundredClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PriceButton(
            priceUnit = stringResource(price_button_1000),
            onClick = onAddOneClick,
        )
        PriceButton(
            priceUnit = stringResource(price_button_5000),
            onClick = onAddFiveClick,
        )
        PriceButton(
            priceUnit = stringResource(price_button_10000),
            onClick = onAddTenClick,
        )
        PriceButton(
            priceUnit = stringResource(price_button_100000),
            onClick = onAddHundredClick,
        )
    }
}

@Composable
private fun PriceButton(
    priceUnit: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = priceUnit,
        style = NapzakMarketTheme.typography.caption12m.copy(
            color = NapzakMarketTheme.colors.gray100,
        ),
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = NapzakMarketTheme.colors.gray10)
            .border(
                width = 1.dp,
                color = NapzakMarketTheme.colors.gray100,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .noRippleClickable(onClick),
    )
}

@Preview
@Composable
private fun PriceIncrementerPreview() {
    NapzakMarketTheme {
        PriceIncrementer(
            onAddOneClick = {},
            onAddFiveClick = {},
            onAddTenClick = {},
            onAddHundredClick = {},
        )
    }
}
