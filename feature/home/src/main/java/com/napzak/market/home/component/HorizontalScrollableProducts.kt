package com.napzak.market.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.component.productItem.NapzakSmallProductItem
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.product.model.Product
import com.napzak.market.ui_util.formatToPriceString
import com.napzak.market.ui_util.noRippleClickable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private const val PRODUCT_WIDTH_RATIO = 116 / 360f
private const val PRODUCT_HEIGHT_RATIO = 116 / 182f

@Composable
internal fun HorizontalScrollableProducts(
    title: String,
    subTitle: String,
    products: ImmutableList<Product>,
    onLikeClick: (Long, Boolean) -> Unit,
    onProductClick: (Long, Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier,
    ) {
        Text(
            text = title,
            style = NapzakMarketTheme.typography.title18b.copy(
                color = NapzakMarketTheme.colors.gray500,
            ),
        )

        Text(
            text = subTitle,
            style = NapzakMarketTheme.typography.caption12m.copy(
                color = NapzakMarketTheme.colors.gray300,
            ),
        )
    }

    ProductsRow(
        modifier = Modifier.padding(top = 18.dp),
        products = products,
        onItemClick = onProductClick,
        onLikeClick = onLikeClick,
    )
}

@Composable
private fun ProductsRow(
    products: ImmutableList<Product>,
    onLikeClick: (Long, Boolean) -> Unit,
    onItemClick: (Long, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp // 화면 가로 크기 (dp 단위)
    val productWidth = remember(screenWidth) {
        (screenWidth * PRODUCT_WIDTH_RATIO)
    }

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 20.dp),
    ) {
        itemsIndexed(
            items = products,
            key = { _, item -> item.productId },
        ) { index, product ->
            with(product) {
                val isSellElseBuy = TradeType.fromName(tradeType) == TradeType.SELL

                NapzakSmallProductItem(
                    title = productName,
                    genre = genreName,
                    imgUrl = photo,
                    price = price.toString().formatToPriceString(),
                    isSellElseBuy = isSellElseBuy,
                    isLiked = isInterested,
                    isMyItem = isOwnedByCurrentUser,
                    isSuggestionAllowed = isPriceNegotiable,
                    onLikeClick = { onLikeClick(productId, product.isInterested) },
                    modifier = Modifier
                        .width(productWidth.dp)
                        .aspectRatio(PRODUCT_HEIGHT_RATIO)
                        .noRippleClickable {
                            onItemClick(productId, index)
                        },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HorizontalScrollableProductsPreview() {
    NapzakMarketTheme {
        Column {
            HorizontalScrollableProducts(
                products = listOf<Product>().toImmutableList(),
                title = "납자기님을 위한 맞춤 PICK!",
                subTitle = "납자기님의 취향에 딱 맞는 아이템들을 모아봤어요.",
                onLikeClick = { _, _ -> },
                onProductClick = { _, _ -> },
            )
        }
    }
}
