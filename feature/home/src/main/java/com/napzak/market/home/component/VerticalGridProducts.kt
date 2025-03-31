package com.napzak.market.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.common.type.TradeStatusType
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.R.drawable.ic_home_forward
import com.napzak.market.designsystem.component.productItem.NapzakLargeProductItem
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.home.R.string.home_button_more
import com.napzak.market.home.model.Product
import com.napzak.market.util.android.noRippleClickable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private const val GRID_CELL_COUNT = 2

@Composable
internal fun VerticalGridProducts(
    title: String,
    subTitle: String,
    products: ImmutableList<Product>,
    onMoreClick: () -> Unit,
    onLikeClick: (Long, Boolean) -> Unit,
    onProductClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = NapzakMarketTheme.typography.title18b.copy(
                color = NapzakMarketTheme.colors.gray500,
            ),
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
        ) {
            Text(
                text = subTitle,
                style = NapzakMarketTheme.typography.caption12m.copy(
                    color = NapzakMarketTheme.colors.gray300,
                ),
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .semantics { role = Role.Button }
                    .noRippleClickable { onMoreClick() },
            ) {
                Text(
                    text = stringResource(home_button_more),
                    style = NapzakMarketTheme.typography.caption12m.copy(
                        color = NapzakMarketTheme.colors.gray300,
                    ),
                )

                Icon(
                    imageVector = ImageVector.vectorResource(ic_home_forward),
                    contentDescription = null,
                    tint = NapzakMarketTheme.colors.gray300,
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            products.chunked(GRID_CELL_COUNT).forEach { productRow ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    productRow.forEach { product ->
                        with(product) {
                            NapzakLargeProductItem(
                                genre = genre,
                                title = name,
                                imgUrl = photo,
                                price = price.toString(),
                                createdDate = uploadTime,
                                reviewCount = reviewCount.toString(),
                                likeCount = likeCount.toString(),
                                isLiked = isInterested,
                                isMyItem = isOwnedByCurrentUser,
                                isSellElseBuy = TradeType.valueOf(tradeType) == TradeType.SELL,
                                isSuggestionAllowed = isPriceNegotiable,
                                tradeStatus = TradeStatusType.get(
                                    tradeStatus, TradeType.valueOf(tradeType)
                                ),
                                onLikeClick = { onLikeClick(id, isInterested) },
                                modifier = Modifier
                                    .weight(1f)
                                    .noRippleClickable { onProductClick(id) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun VerticalGridProductsPreview() {
    NapzakMarketTheme {
        Column {
            VerticalGridProducts(
                products = Product.mockMixedProduct.toImmutableList(),
                title = "지금 가장 많이 찜한 납작템",
                subTitle = "놓치면 아쉬운 인기 아이템들을 구경해볼까요?",
                onLikeClick = { _, _ -> },
                onProductClick = { },
                onMoreClick = {},
                modifier = Modifier
                    .padding(top = 30.dp)
                    .background(color = NapzakMarketTheme.colors.gray10)
                    .padding(start = 20.dp, end = 20.dp, top = 32.dp, bottom = 20.dp),
            )
        }
    }
}
