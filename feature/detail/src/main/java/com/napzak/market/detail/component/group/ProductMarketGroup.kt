package com.napzak.market.detail.component.group

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.designsystem.R.drawable.ic_white_arrow_right
import com.napzak.market.designsystem.R.drawable.ic_circle_purple_user
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.detail.R.string.detail_product_market_buy
import com.napzak.market.feature.detail.R.string.detail_product_market_count
import com.napzak.market.feature.detail.R.string.detail_product_market_group_title
import com.napzak.market.feature.detail.R.string.detail_product_market_sell
import com.napzak.market.ui_util.noRippleClickable

@Composable
internal fun ProductMarketGroup(
    marketImage: String,
    marketName: String,
    sellCount: String,
    buyCount: String,
    onMarketProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .padding(top = 26.dp, bottom = 22.dp),
    ) {
        Text(
            text = stringResource(detail_product_market_group_title),
            style = NapzakMarketTheme.typography.body14b.copy(
                color = NapzakMarketTheme.colors.gray500,
            ),
        )

        Spacer(Modifier.height(20.dp))

        ProductMarketProfile(
            marketImage = marketImage,
            marketName = marketName,
            sellCount = sellCount,
            buyCount = buyCount,
            modifier = Modifier.noRippleClickable(onClick = onMarketProfileClick),
        )
    }
}

@Composable
private fun ProductMarketProfile(
    marketImage: String,
    marketName: String,
    sellCount: String,
    buyCount: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val marketCounts = listOf(
        detail_product_market_sell to sellCount,
        detail_product_market_buy to buyCount,
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .semantics(mergeDescendants = true) { contentDescription = marketName }
            .background(color = NapzakMarketTheme.colors.gray10, shape = RoundedCornerShape(25.dp))
            .padding(horizontal = 22.dp, vertical = 17.dp),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .placeholder(ic_circle_purple_user)
                .error(ic_circle_purple_user)
                .fallback(ic_circle_purple_user)
                .data(marketImage.ifBlank { null })
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(shape = CircleShape),
        )

        Spacer(Modifier.width(14.dp))

        Column {
            Text(
                text = marketName,
                style = NapzakMarketTheme.typography.body14b.copy(
                    color = NapzakMarketTheme.colors.purple500,
                ),
            )
            Spacer(Modifier.height(7.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                marketCounts.forEach { (type, count) ->
                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = stringResource(type),
                            style = NapzakMarketTheme.typography.caption12m.copy(
                                color = NapzakMarketTheme.colors.gray500,
                            ),
                        )
                        Text(
                            text = stringResource(detail_product_market_count, count),
                            style = NapzakMarketTheme.typography.caption12m.copy(
                                color = NapzakMarketTheme.colors.gray500,
                            ),
                        )
                    }
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Icon(
            imageVector = ImageVector.vectorResource(ic_white_arrow_right),
            contentDescription = null,
            tint = NapzakMarketTheme.colors.gray300,
            modifier = Modifier.padding(start = 10.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductMarketGroupPreview() {
    NapzakMarketTheme {
        ProductMarketGroup(
            marketImage = "",
            marketName = "납작한 자기",
            sellCount = "31",
            buyCount = "15",
            modifier = Modifier.background(NapzakMarketTheme.colors.white),
            onMarketProfileClick = {},
        )
    }
}
