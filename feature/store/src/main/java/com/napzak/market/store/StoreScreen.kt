package com.napzak.market.store

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.common.type.MarketTab
import com.napzak.market.common.type.SortType
import com.napzak.market.common.type.TradeStatusType
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.component.bottomsheet.Genre
import com.napzak.market.designsystem.component.productItem.NapzakLargeProductItem
import com.napzak.market.designsystem.component.tabbar.MarketTabBar
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.store.model.StoreInfo
import com.napzak.market.util.android.noRippleClickable
import com.napzak.market.feature.store.R.drawable.ic_left_chevron
import com.napzak.market.feature.store.R.drawable.ic_down_chevron_7
import com.napzak.market.feature.store.R.string.store_filter_selling
import com.napzak.market.feature.store.R.string.store_filter_buying
import com.napzak.market.feature.store.R.string.store_product
import com.napzak.market.feature.store.R.string.store_count
import com.napzak.market.feature.store.R.string.store_empty_text
import com.napzak.market.store.component.BasicFilterChip
import com.napzak.market.store.component.GenreChip
import com.napzak.market.store.component.GenreFilterChip
import com.napzak.market.store.component.GenreNavigationButton
import com.napzak.market.store.model.Product
import kotlin.collections.chunked
import kotlin.collections.forEach

@Composable
internal fun StoreRoute(modifier: Modifier = Modifier) {

}

@Composable
private fun StoreScreen(
    storeInfo: StoreInfo,
    selectedTab: MarketTab,
    filteredGenres: List<Genre>,
    sortType: SortType,
    productList: List<Product> = Product.mockMixedProduct,
    onProfileEditClick: () -> Unit,
    onTabClicked: (MarketTab) -> Unit,
    onGenreFilterClick: () -> Unit,
    onFilterClick: () -> Unit,
    onBackButtonClick: () -> Unit,
    onGenreDetailNavigate: (Long) -> Unit,
    onSortOptionClick: (SortType) -> Unit,
    onProductDetailNavigate: (Long) -> Unit,
    onLikeButtonClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NapzakMarketTheme.colors.white),
    ) {
        StoreTopBar(
            onBackButtonClick = onBackButtonClick,
        )

        StoreScrollSection(
            storeInfo = storeInfo,
            selectedTab = selectedTab,
            filteredGenres = filteredGenres,
            sortType = sortType,
            productList = productList,
            onProfileEditClick = onProfileEditClick,
            onTabClicked = onTabClicked,
            onGenreFilterClick = onGenreFilterClick,
            onFilterClick = onFilterClick,
            onGenreDetailNavigate = onGenreDetailNavigate,
            onSortOptionClick = onSortOptionClick,
            onProductDetailNavigate = onProductDetailNavigate,
            onLikeButtonClick = onLikeButtonClick,
        )
    }
}

@Composable
private fun StoreTopBar(
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.padding(start = 20.dp, top = 62.dp, bottom = 22.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(ic_left_chevron),
            contentDescription = null,
            tint = NapzakMarketTheme.colors.gray200,
            modifier = Modifier.noRippleClickable(onBackButtonClick),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun StoreScrollSection(
    storeInfo: StoreInfo,
    selectedTab: MarketTab,
    filteredGenres: List<Genre>,
    productList: List<Product>,
    sortType: SortType,
    onProfileEditClick: () -> Unit,
    onTabClicked: (MarketTab) -> Unit,
    onGenreFilterClick: () -> Unit,
    onFilterClick: () -> Unit,
    onGenreDetailNavigate: (Long) -> Unit,
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
                storeInfo = storeInfo,
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
                    .padding(horizontal = 20.dp),
            )

            if (selectedTab != MarketTab.REVIEW) {
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
                        isClicked = false,
                        onChipClick = onFilterClick,
                    )
                }
            }
        }

        item {
            Column {
                if (filteredGenres.isNotEmpty()) {
                    filteredGenres.forEach { genreItem ->
                        GenreNavigationButton(
                            genreName = genreItem.genreName,
                            onBlockClick = { onGenreDetailNavigate(genreItem.genreId) },
                        )

                        Spacer(
                            Modifier
                                .fillMaxWidth()
                                .background(color = NapzakMarketTheme.colors.gray10)
                                .height(4.dp),
                        )
                    }
                }

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
                                append(stringResource(store_count, productList.size))
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
                            onLikeClick = { onLikeButtonClick(id, isInterested) },
                            modifier = Modifier
                                .weight(1f)
                                .noRippleClickable { onProductDetailNavigate(id) },
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

@Composable
private fun StoreInfoSection(
    storeInfo: StoreInfo,
    onProfileEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val userId: Long = 0 // TODO: 추후 userId로 변경

    with(storeInfo) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.BottomCenter,
        ) {
            Column {
                Box(
                    contentAlignment = Alignment.BottomEnd,
                ) {
                    AsyncImage(
                        model = ImageRequest
                            .Builder(context)
                            .data(storeCover)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2.25f)
                            .background(color = NapzakMarketTheme.colors.gray100),
                    )

                    if (storeId == userId) {
                        Box(
                            modifier = Modifier
                                .padding(end = 20.dp, bottom = 10.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "프로필 편집",
                                style = NapzakMarketTheme.typography.caption10sb,
                                color = NapzakMarketTheme.colors.white,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .noRippleClickable(onProfileEditClick)
                                    .background(
                                        color = NapzakMarketTheme.colors.transBlack,
                                        shape = RoundedCornerShape(24.dp)
                                    )
                                    .padding(6.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(35.dp))
            }

            AsyncImage(
                model = ImageRequest
                    .Builder(context)
                    .data(storePhoto)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .background(
                        color = NapzakMarketTheme.colors.gray200,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .border(
                        width = 5.dp,
                        color = NapzakMarketTheme.colors.white,
                        shape = RoundedCornerShape(50.dp)
                    ),
            )
        }

        Spacer(Modifier.height(9.dp))

        Text(
            text = storeNickName,
            style = NapzakMarketTheme.typography.body16sb,
            color = NapzakMarketTheme.colors.gray500,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = storeDescription,
            style = NapzakMarketTheme.typography.caption10sb,
            color = NapzakMarketTheme.colors.gray500,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
        )

        Spacer(Modifier.height(20.dp))

        LazyRow(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(genrePreferences) { genre ->
                GenreChip(
                    genreName = genre.genreName,
                )
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Preview
@Composable
private fun StoreScreenPreview(modifier: Modifier = Modifier) {
    val storeInfo = StoreInfo(
        storeId = 1,
        storeNickName = "납작핑",
        storeDescription = "잡덕입니다. 최애는 짱구, 철수, 흰둥이, 긴토키, 히지카타, 고죠 사토루 관련 상품 판매 및 구매 제시 채팅 언제든 환영합니다 :)",
        storePhoto = "",
        storeCover = "",
        isStoreOwner = true,
        genrePreferences = listOf(
            Genre(0, "산리오0"),
            Genre(1, "산리오1"),
            Genre(2, "산리오2"),
            Genre(3, "산리오3"),
            Genre(4, "산리오4"),
            Genre(5, "산리오5"),
            Genre(6, "산리오6")
        )
    )

    NapzakMarketTheme {
        StoreScreen(
            selectedTab = MarketTab.SELL,
            storeInfo = storeInfo,
            filteredGenres = emptyList(),
            sortType = SortType.RECENT,
            onBackButtonClick = {},
            onProfileEditClick = {},
            onTabClicked = {},
            onGenreFilterClick = {},
            onFilterClick = {},
            onGenreDetailNavigate = {},
            onSortOptionClick = {},
            onProductDetailNavigate = {},
            onLikeButtonClick = { id, value -> },
            modifier = modifier,
        )
    }
}