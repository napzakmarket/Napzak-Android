package com.napzak.market.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.common.type.TradeStatusType
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.R.drawable.ic_left_chevron
import com.napzak.market.designsystem.component.productItem.NapzakLargeProductItem
import com.napzak.market.designsystem.component.tabbar.TradeTypeTabBar
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.wishlist.R.string.wishlist_back_button
import com.napzak.market.feature.wishlist.R.string.wishlist_title
import com.napzak.market.product.model.Product
import com.napzak.market.ui_util.noRippleClickable
import kotlin.collections.chunked

@Composable
internal fun WishlistRoute(
    modifier: Modifier = Modifier,
) {

    WishlistScreen(
        selectedTab = TradeType.SELL,
        products = emptyList(),
        onBackButtonClick = {},
        onTabClick = {},
        onProductDetailNavigate = {},
        onLikeButtonClick = { id, value -> },
        modifier = modifier,
    )
}

@Composable
private fun WishlistScreen(
    selectedTab: TradeType,
    products: List<Product>,
    onBackButtonClick: () -> Unit,
    onTabClick: (TradeType) -> Unit,
    onProductDetailNavigate: (Long) -> Unit,
    onLikeButtonClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NapzakMarketTheme.colors.white)
            .padding(top = 58.dp),
    ) {
        Row(
            modifier = Modifier.padding(start = 20.dp, bottom = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(ic_left_chevron),
                contentDescription = stringResource(wishlist_back_button),
                tint = NapzakMarketTheme.colors.black,
                modifier = Modifier.noRippleClickable(onBackButtonClick)
            )

            Spacer(Modifier.width(4.dp))

            Text(
                text = stringResource(wishlist_title),
                style = NapzakMarketTheme.typography.body16b.copy(
                    color = NapzakMarketTheme.colors.black,
                )
            )
        }

        TradeTypeTabBar(
            selectedTab = selectedTab,
            onTabClicked = onTabClick,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        WishlistProducts(
            products = products,
            onProductClick = onProductDetailNavigate,
            onLikeButtonClick = onLikeButtonClick,
        )
    }
}

@Composable
private fun WishlistProducts(
    products: List<Product>,
    onProductClick: (Long) -> Unit,
    onLikeButtonClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = NapzakMarketTheme.colors.white),
    ) {
        itemsIndexed(products.chunked(2)) { index, rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                rowItems.forEach { product ->
                    with(product) {
                        NapzakLargeProductItem(
                            genre = genreName,
                            title = productName,
                            imgUrl = photo,
                            price = price.toString(),
                            createdDate = uploadTime,
                            reviewCount = chatCount.toString(),
                            likeCount = interestCount.toString(),
                            isLiked = isInterested,
                            isMyItem = isOwnedByCurrentUser,
                            isSellElseBuy = TradeType.valueOf(tradeType) == TradeType.SELL,
                            isSuggestionAllowed = isPriceNegotiable,
                            tradeStatus = TradeStatusType.get(
                                tradeStatus, TradeType.valueOf(tradeType)
                            ),
                            onLikeClick = { onLikeButtonClick(productId, isInterested) },
                            modifier = Modifier
                                .weight(1f)
                                .noRippleClickable { onProductClick(productId) },
                        )
                    }
                }

                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        item {
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Preview
@Composable
private fun WishlistScreenPreview(modifier: Modifier = Modifier) {
    NapzakMarketTheme {
        WishlistScreen(
            selectedTab = TradeType.SELL,
            products = emptyList(),
            onBackButtonClick = {},
            onTabClick = {},
            onProductDetailNavigate = {},
            onLikeButtonClick = { id, value -> },
        )
    }
}
