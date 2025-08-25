package com.napzak.market.registration.purchase.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_square_purple_check
import com.napzak.market.designsystem.R.drawable.ic_square_gray_unchecked
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.registration.R.string.accept_negotiation
import com.napzak.market.feature.registration.R.string.negotiation
import com.napzak.market.feature.registration.R.string.negotiation_description
import com.napzak.market.ui_util.noRippleClickable

@Composable
internal fun PriceNegotiationGroup(
    isNegotiable: Boolean,
    onNegotiableChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val checkIcon = if (isNegotiable) ic_square_purple_check else ic_square_gray_unchecked

    Column(
        modifier = Modifier
            .background(NapzakMarketTheme.colors.gray10)
            .then(modifier),
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(negotiation),
            style = NapzakMarketTheme.typography.body14b.copy(
                color = NapzakMarketTheme.colors.gray500,
            ),
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(negotiation_description),
            style = NapzakMarketTheme.typography.caption12m.copy(
                color = NapzakMarketTheme.colors.gray300,
            ),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(checkIcon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.noRippleClickable {
                    onNegotiableChange(!isNegotiable)
                },
            )
            Text(
                text = stringResource(accept_negotiation),
                style = NapzakMarketTheme.typography.body14b.copy(
                    color = NapzakMarketTheme.colors.gray500,
                ),
            )
        }

        Spacer(modifier = Modifier.height(56.dp))
    }
}

@Preview
@Composable
private fun PriceNegotiationGroupPreview() {
    NapzakMarketTheme {
        var isNegotiable by remember { mutableStateOf(false) }
        PriceNegotiationGroup(
            isNegotiable = isNegotiable,
            onNegotiableChange = { isNegotiable = it },
        )
    }
}
