package com.napzak.market.detail.component.group

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.common.type.TradeStatusType
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.detail.R.drawable.ic_product_buy_complete_large
import com.napzak.market.feature.detail.R.drawable.ic_product_reservation_large
import com.napzak.market.feature.detail.R.drawable.ic_product_sell_complete_large
import com.napzak.market.feature.detail.R.string.detail_product_image_indicator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private const val HEIGHT_RATIO = 360f / 400

@Composable
internal fun ProductImageGroup(
    imageUrls: ImmutableList<String>,
    contentDescription: String,
    tradeStatusType: TradeStatusType,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { Int.MAX_VALUE }
    )
    val currentPage = remember(pagerState.currentPage) {
        pagerState.currentPage % imageUrls.size
    }

    Box(
        modifier
            .fillMaxWidth()
            .aspectRatio(HEIGHT_RATIO)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = ImageRequest
                    .Builder(context)
                    .data(imageUrls[currentPage])
                    .crossfade(true)
                    .build(),
                contentDescription = contentDescription,
                contentScale = ContentScale.FillBounds,
            )
        }

        TradeStatusCoverImage(
            tradeStatusType = tradeStatusType,
        )

        PageNumberIndicator(
            currentPage = currentPage,
            totalPage = imageUrls.size,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 22.dp, bottom = 20.dp),
        )
    }
}

@Composable
private fun PageNumberIndicator(
    currentPage: Int,
    totalPage: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(detail_product_image_indicator, (currentPage + 1), totalPage),
        style = NapzakMarketTheme.typography.caption10r,
        color = NapzakMarketTheme.colors.white,
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(color = NapzakMarketTheme.colors.transBlack)
            .padding(horizontal = 11.dp, vertical = 3.dp),
    )
}

@Composable
private fun TradeStatusCoverImage(
    tradeStatusType: TradeStatusType,
    modifier: Modifier = Modifier,
) {
    val iconRes = when (tradeStatusType) {
        TradeStatusType.BEFORE_TRADE -> null
        TradeStatusType.RESERVED -> ic_product_reservation_large
        TradeStatusType.COMPLETED_SELL -> ic_product_sell_complete_large
        TradeStatusType.COMPLETED_BUY -> ic_product_buy_complete_large
    }

    if (iconRes != null) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .background(NapzakMarketTheme.colors.transBlack),
        ) {
            Image(
                imageVector = ImageVector.vectorResource(iconRes),
                contentDescription = tradeStatusType.label,
            )
            Text(
                text = tradeStatusType.label,
                style = NapzakMarketTheme.typography.title20sb,
                color = NapzakMarketTheme.colors.white,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductImageGroupPreview() {
    NapzakMarketTheme {
        ProductImageGroup(
            imageUrls = listOf(
                "",
                "",
                "",
            ).toImmutableList(),
            contentDescription = "",
            tradeStatusType = TradeStatusType.BEFORE_TRADE,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReservedProductImageGroup() {
    NapzakMarketTheme {
        ProductImageGroup(
            imageUrls = listOf(
                "",
                "",
                "",
            ).toImmutableList(),
            contentDescription = "",
            tradeStatusType = TradeStatusType.RESERVED,
        )
    }
}