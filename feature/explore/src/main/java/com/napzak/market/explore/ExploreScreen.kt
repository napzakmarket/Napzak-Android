package com.napzak.market.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.common.type.SortType
import com.napzak.market.common.type.TradeStatusType
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.R.drawable.ic_down_chevron
import com.napzak.market.designsystem.component.bottomsheet.Genre
import com.napzak.market.designsystem.component.productItem.NapzakLargeProductItem
import com.napzak.market.designsystem.component.tabbar.TradeTypeTabBar
import com.napzak.market.designsystem.component.textfield.SearchTextField
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.explore.component.BasicFilterChip
import com.napzak.market.explore.component.GenreFilterChip
import com.napzak.market.explore.component.GenreNavigationButton
import com.napzak.market.explore.model.Product
import com.napzak.market.feature.explore.R.string.explore_search_hint
import com.napzak.market.feature.explore.R.string.explore_unopened
import com.napzak.market.feature.explore.R.string.explore_exclude_sold_out
import com.napzak.market.feature.explore.R.string.explore_product
import com.napzak.market.feature.explore.R.string.explore_count
import com.napzak.market.util.android.noRippleClickable

@Composable
internal fun ExploreRoute(
    searchTerm: String,
    onSearchNavigate: () -> Unit,
    onGenreDetailNavigate: (Long) -> Unit,
    onProductDetailNavigate: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    ExploreScreen(
        searchTerm = searchTerm,
        onSearchNavigate = onSearchNavigate,
        onTabClick = { },
        onGenreFilterClick = { },
        onUnopenFilterClick = { },
        onExcludeSoldOutFilterClick = { },
        onGenreDetailNavigate = onGenreDetailNavigate,
        onSortOptionClick = { },
        onProductDetailNavigate = onProductDetailNavigate,
        onLikeButtonClick = { id, value -> },
        modifier = modifier,
    )
}

@Composable
private fun ExploreScreen(
    searchTerm: String,
    onSearchNavigate: () -> Unit,
    onTabClick: (TradeType) -> Unit,
    onGenreFilterClick: () -> Unit,
    onUnopenFilterClick: () -> Unit,
    onExcludeSoldOutFilterClick: () -> Unit,
    onSortOptionClick: (SortType) -> Unit,
    onGenreDetailNavigate: (Long) -> Unit,
    onProductDetailNavigate: (Long) -> Unit,
    onLikeButtonClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO : 더미데이터 변수 삭제 예정
    var tradeType = TradeType.BUY
    val genreList = listOf(
        Genre(0, "산리오"),
        Genre(1, "주술회전"),
        Genre(2, "진격의 거인"),
        Genre(3, "산리오1"),
        Genre(4, "주술회전1"),
        Genre(5, "진격의 거인1"),
    )
    var sortType = SortType.RECENT
    val searchTerm = searchTerm

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NapzakMarketTheme.colors.white)
            .padding(top = 54.dp),
    ) {
        ExploreSearchTextField(
            searchTerm = searchTerm,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .noRippleClickable(onSearchNavigate),
        )

        Spacer(Modifier.height(20.dp))

        TradeTypeTabBar(
            selectedTab = tradeType,
            onTabClicked = onTabClick,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = NapzakMarketTheme.colors.gray10)
                .padding(horizontal = 20.dp)
                .padding(top = 15.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            GenreFilterChip(
                genreList = genreList,
                onChipClick = onGenreFilterClick,
            )

            BasicFilterChip(
                filterName = stringResource(explore_unopened),
                isClicked = false,
                onChipClick = onUnopenFilterClick,
            )

            if (tradeType == TradeType.SELL) {
                BasicFilterChip(
                    filterName = stringResource(explore_exclude_sold_out),
                    isClicked = false,
                    onChipClick = onExcludeSoldOutFilterClick,
                )
            }
        }

        GenreAndProductList(
            genreList = genreList,
            productList = Product.mockMixedProduct,
            sortType = sortType,
            onGenreButtonClick = onGenreDetailNavigate,
            onSortOptionClick = { onSortOptionClick(sortType) },
            onProductClick = onProductDetailNavigate,
            onLikeButtonClick = onLikeButtonClick,
        )
    }
}

@Composable
private fun ExploreSearchTextField(
    searchTerm: String,
    modifier: Modifier = Modifier,
) {
    SearchTextField(
        text = searchTerm,
        onTextChange = { },
        hint = stringResource(explore_search_hint),
        onResetClick = { },
        onSearchClick = { },
        modifier = modifier,
        readOnly = true,
    )
}

@Composable
private fun GenreAndProductList(
    genreList: List<Genre>,
    productList: List<Product>,
    sortType: SortType,
    onGenreButtonClick: (Long) -> Unit,
    onSortOptionClick: () -> Unit,
    onProductClick: (Long) -> Unit,
    onLikeButtonClick: (Long, Boolean) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = NapzakMarketTheme.colors.white),
    ) {
        item {
            Column {
                if (genreList.isNotEmpty()) {
                    genreList.forEach { genreItem ->
                        GenreNavigationButton(
                            genreName = genreItem.genreName,
                            onBlockClick = { onGenreButtonClick(genreItem.genreId) },
                        )

                        HorizontalDivider(
                            thickness = 4.dp,
                            color = NapzakMarketTheme.colors.gray10
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
                                append(stringResource(explore_product))
                            }
                            withStyle(style = SpanStyle(color = NapzakMarketTheme.colors.purple500)) {
                                append(stringResource(explore_count, productList.size))
                            }
                        },
                        style = NapzakMarketTheme.typography.body14sb,
                    )

                    Spacer(Modifier.weight(1f))

                    Row(
                        modifier = Modifier.noRippleClickable(onSortOptionClick),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = sortType.label,
                            style = NapzakMarketTheme.typography.caption12sb,
                            color = NapzakMarketTheme.colors.gray200,
                        )

                        Spacer(Modifier.width(3.dp))

                        Icon(
                            imageVector = ImageVector.vectorResource(ic_down_chevron),
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
                                .noRippleClickable { onProductClick(id) },
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
private fun ExploreScreenPreview(modifier: Modifier = Modifier) {
    NapzakMarketTheme {
        ExploreScreen(
            searchTerm = "",
            onSearchNavigate = { },
            onTabClick = { },
            onGenreFilterClick = { },
            onUnopenFilterClick = { },
            onExcludeSoldOutFilterClick = { },
            onSortOptionClick = { },
            onGenreDetailNavigate = { },
            onProductDetailNavigate = { },
            onLikeButtonClick = { id, value -> },
            modifier = modifier
        )
    }
}