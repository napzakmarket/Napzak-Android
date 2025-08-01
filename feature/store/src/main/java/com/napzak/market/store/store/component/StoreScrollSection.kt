package com.napzak.market.store.store.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.napzak.market.common.type.MarketTab
import com.napzak.market.common.type.SortType
import com.napzak.market.common.type.TradeStatusType
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.component.GenreFilterChip
import com.napzak.market.designsystem.component.productItem.NapzakLargeProductItem
import com.napzak.market.designsystem.component.tabbar.MarketTabBar
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.store.R.drawable.ic_down_chevron_7
import com.napzak.market.feature.store.R.string.store_count
import com.napzak.market.feature.store.R.string.store_empty_text
import com.napzak.market.feature.store.R.string.store_filter_buying
import com.napzak.market.feature.store.R.string.store_filter_selling
import com.napzak.market.feature.store.R.string.store_product
import com.napzak.market.genre.model.Genre
import com.napzak.market.product.model.Product
import com.napzak.market.store.model.StoreDetail
import com.napzak.market.ui_util.ShadowDirection
import com.napzak.market.ui_util.napzakGradientShadow
import com.napzak.market.ui_util.noRippleClickable


@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun StoreScrollSection(
    storeDetail: StoreDetail,
    isOwner: Boolean,
    selectedTab: MarketTab,
    filteredGenres: List<Genre>,
    isOnSale: Boolean,
    productCount: Int,
    productList: List<Product>,
    sortType: SortType,
    onProfileEditClick: () -> Unit,
    onTabClicked: (MarketTab) -> Unit,
    onGenreFilterClick: () -> Unit,
    onFilterClick: () -> Unit,
    onSortOptionClick: (SortType) -> Unit,
    onProductDetailNavigate: (Long) -> Unit,
    onLikeButtonClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val filterChipName = when (selectedTab) {
        MarketTab.BUY -> stringResource(store_filter_buying)
        MarketTab.SELL -> stringResource(store_filter_selling)
        MarketTab.REVIEW -> stringResource(store_empty_text)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = NapzakMarketTheme.colors.white),
    ) {
        item {
            StoreInfoSection(
                storeDetail = storeDetail,
                isMyStore = isOwner,
                onProfileEditClick = onProfileEditClick,
            )
        }

        stickyHeader {
            MarketTabBar(
                selectedTab = selectedTab,
                onTabClicked = onTabClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = NapzakMarketTheme.colors.white)
                    .padding(horizontal = 20.dp)
                    .napzakGradientShadow(
                        height = 4.dp,
                        startColor = NapzakMarketTheme.colors.shadowBlack,
                        endColor = NapzakMarketTheme.colors.transWhite,
                        direction = ShadowDirection.Bottom,
                    ),
            )

            if (selectedTab != MarketTab.REVIEW && productCount != 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = NapzakMarketTheme.colors.gray10)
                        .padding(horizontal = 20.dp)
                        .padding(top = 15.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    GenreFilterChip(
                        genreList = filteredGenres,
                        onChipClick = onGenreFilterClick,
                    )

                    BasicFilterChip(
                        filterName = filterChipName,
                        isClicked = isOnSale,
                        onChipClick = onFilterClick,
                    )
                }
            }
        }

        if (selectedTab != MarketTab.REVIEW) {
            if (productCount == 0) {
                item {
                    StoreEmptyView(Modifier.fillMaxSize())
                }
            } else {
                item {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, top = 20.dp, end = 20.dp),
                        ) {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(color = NapzakMarketTheme.colors.gray500)) {
                                        append(stringResource(store_product))
                                    }
                                    withStyle(style = SpanStyle(color = NapzakMarketTheme.colors.purple500)) {
                                        append(stringResource(store_count, productCount))
                                    }
                                },
                                style = NapzakMarketTheme.typography.body14sb,
                            )

                            Spacer(Modifier.weight(1f))

                            Row(
                                modifier = Modifier.noRippleClickable { onSortOptionClick(sortType) },
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = sortType.label,
                                    style = NapzakMarketTheme.typography.caption12sb,
                                    color = NapzakMarketTheme.colors.gray200,
                                )

                                Spacer(Modifier.width(3.dp))

                                Icon(
                                    imageVector = ImageVector.vectorResource(ic_down_chevron_7),
                                    contentDescription = null,
                                    tint = NapzakMarketTheme.colors.gray200,
                                    modifier = Modifier.size(width = 7.dp, height = 4.dp),
                                )
                            }
                        }
                    }
                }

                itemsIndexed(productList.chunked(2)) { index, rowItems ->
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
                                        .noRippleClickable { onProductDetailNavigate(productId) },
                                )
                            }
                        }

                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

                item {
                    Spacer(Modifier.padding(bottom = 32.dp))
                }
            }
        }
    }
}