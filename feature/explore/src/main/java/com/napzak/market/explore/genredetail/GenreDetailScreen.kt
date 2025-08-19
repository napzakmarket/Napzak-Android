package com.napzak.market.explore.genredetail

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.SortType
import com.napzak.market.common.type.TradeStatusType
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.R.drawable.ic_down_chevron
import com.napzak.market.designsystem.component.bottomsheet.SortBottomSheet
import com.napzak.market.designsystem.component.loading.NapzakLoadingOverlay
import com.napzak.market.designsystem.component.productItem.NapzakLargeProductItem
import com.napzak.market.designsystem.component.tabbar.TradeTypeTabBar
import com.napzak.market.designsystem.component.topbar.NapzakBasicTopBar
import com.napzak.market.designsystem.component.topbar.NapzakTopBarAction
import com.napzak.market.designsystem.component.topbar.NapzakTopBarColor
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.explore.component.BasicFilterChip
import com.napzak.market.explore.component.GenreLabel
import com.napzak.market.explore.genredetail.state.GenreDetailUiState
import com.napzak.market.feature.explore.R.drawable.ic_home
import com.napzak.market.feature.explore.R.drawable.ic_left_chevron_24
import com.napzak.market.feature.explore.R.string.explore_count
import com.napzak.market.feature.explore.R.string.explore_exclude_sold_out
import com.napzak.market.feature.explore.R.string.explore_product
import com.napzak.market.feature.explore.R.string.explore_unopened
import com.napzak.market.genre.model.GenreInfo
import com.napzak.market.product.model.Product
import com.napzak.market.ui_util.noRippleClickable

@Composable
internal fun GenreDetailRoute(
    onBackButtonClick: () -> Unit,
    onHomeNavigate: () -> Unit,
    onProductClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GenreDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var sortBottomSheetState by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.updateGenreInfo()
    }

    LaunchedEffect(
        uiState.selectedTab,
        uiState.isUnopenSelected,
        uiState.isSoldOutSelected,
        uiState.sortOption,
    ) {
        viewModel.updateGenreDetailInformation()
    }

    GenreDetailScreen(
        uiState = uiState,
        sortBottomSheetState = sortBottomSheetState,
        onDismissRequest = { sortBottomSheetState = !sortBottomSheetState },
        onBackButtonClick = onBackButtonClick,
        onHomeButtonClick = onHomeNavigate,
        onTabClick = viewModel::updateTradeType,
        onUnopenFilterClick = viewModel::updateUnopenFilter,
        onExcludeSoldOutFilterClick = viewModel::updateSoldOutFilter,
        onProductClick = onProductClick,
        onSortOptionClick = { sortBottomSheetState = !sortBottomSheetState },
        onSortItemClick = { newSortOption ->
            viewModel.updateSortOption(newSortOption)
            sortBottomSheetState = !sortBottomSheetState
        },
        onLikeButtonClick = { id, value ->
            viewModel.updateProductIsInterested(productId = id, isLiked = value)
        },
        modifier = modifier,
    )
}

@Composable
private fun GenreDetailScreen(
    uiState: GenreDetailUiState,
    sortBottomSheetState: Boolean,
    onDismissRequest: () -> Unit,
    onBackButtonClick: () -> Unit,
    onHomeButtonClick: () -> Unit,
    onTabClick: (TradeType) -> Unit,
    onUnopenFilterClick: () -> Unit,
    onExcludeSoldOutFilterClick: () -> Unit,
    onSortOptionClick: () -> Unit,
    onSortItemClick: (SortType) -> Unit,
    onProductClick: (Long) -> Unit,
    onLikeButtonClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState.loadState) {
        is UiState.Loading -> NapzakLoadingOverlay()

        is UiState.Empty -> {
        }

        is UiState.Failure -> {
        }

        is UiState.Success -> {
            with(uiState) {
                GenreDetailSuccessScreen(
                    genreInfo = genreInfo,
                    selectedTab = selectedTab,
                    isUnopenSelected = isUnopenSelected,
                    isSoldOutSelected = isSoldOutSelected,
                    productCount = uiState.loadState.data.productCount,
                    productList = uiState.loadState.data.productList,
                    sortType = sortOption,
                    sortBottomSheetState = sortBottomSheetState,
                    onDismissRequest = onDismissRequest,
                    onBackButtonClick = onBackButtonClick,
                    onHomeButtonClick = onHomeButtonClick,
                    onTabClick = onTabClick,
                    onUnopenFilterClick = onUnopenFilterClick,
                    onExcludeSoldOutFilterClick = onExcludeSoldOutFilterClick,
                    onProductClick = onProductClick,
                    onSortOptionClick = onSortOptionClick,
                    onSortItemClick = onSortItemClick,
                    onLikeButtonClick = onLikeButtonClick,
                    modifier = modifier,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenreDetailSuccessScreen(
    genreInfo: GenreInfo,
    selectedTab: TradeType,
    isUnopenSelected: Boolean,
    isSoldOutSelected: Boolean,
    productCount: Int,
    productList: List<Product>,
    sortType: SortType,
    sortBottomSheetState: Boolean,
    onDismissRequest: () -> Unit,
    onBackButtonClick: () -> Unit,
    onHomeButtonClick: () -> Unit,
    onTabClick: (TradeType) -> Unit,
    onUnopenFilterClick: () -> Unit,
    onExcludeSoldOutFilterClick: () -> Unit,
    onSortOptionClick: () -> Unit,
    onSortItemClick: (SortType) -> Unit,
    onProductClick: (Long) -> Unit,
    onLikeButtonClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NapzakMarketTheme.colors.white),
    ) {
        GenreTopBar(
            onBackButtonClick = onBackButtonClick,
            onHomeButtonClick = onHomeButtonClick,
        )

        GenreScrollSection(
            genreInfo = genreInfo,
            selectedTab = selectedTab,
            isUnopenSelected = isUnopenSelected,
            isSoldOutSelected = isSoldOutSelected,
            productCount = productCount,
            productList = productList,
            sortType = sortType,
            onTabClick = onTabClick,
            onUnopenFilterClick = onUnopenFilterClick,
            onExcludeSoldOutFilterClick = onExcludeSoldOutFilterClick,
            onSortOptionClick = onSortOptionClick,
            onProductClick = onProductClick,
            onLikeButtonClick = onLikeButtonClick,
        )
    }

    if (sortBottomSheetState) {
        SortBottomSheet(
            selectedSortType = sortType,
            sheetState = sheetState,
            onDismissRequest = onDismissRequest,
            onSortItemClick = onSortItemClick,
        )
    }
}

@Composable
private fun GenreTopBar(
    onBackButtonClick: () -> Unit,
    onHomeButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navigators = listOf(
        NapzakTopBarAction(ic_left_chevron_24, onBackButtonClick),
        NapzakTopBarAction(ic_home, onHomeButtonClick)
    )
    NapzakBasicTopBar(
        navigators = navigators,
        isShadowed = true,
        title = null,
        titleAlign = null,
        actions = null,
        color = NapzakTopBarColor(
            iconColor = NapzakMarketTheme.colors.gray200,
            contentColor = NapzakMarketTheme.colors.gray400,
            containerColor = NapzakMarketTheme.colors.white,
        ),
        paddingValues = PaddingValues(start = 13.dp, top = 34.dp, end = 13.dp, bottom = 18.dp),
        modifier = modifier,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GenreScrollSection(
    genreInfo: GenreInfo,
    selectedTab: TradeType,
    isUnopenSelected: Boolean,
    isSoldOutSelected: Boolean,
    productCount: Int,
    productList: List<Product>,
    sortType: SortType,
    onTabClick: (TradeType) -> Unit,
    onUnopenFilterClick: () -> Unit,
    onExcludeSoldOutFilterClick: () -> Unit,
    onSortOptionClick: () -> Unit,
    onProductClick: (Long) -> Unit,
    onLikeButtonClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier,
    ) {
        item {
            with(genreInfo) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.9f),
                    ) {
                        AsyncImage(
                            model = ImageRequest
                                .Builder(context)
                                .data(cover)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = NapzakMarketTheme.colors.gray100),
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp),
                    ) {
                        GenreLabel()

                        tag?.let {
                            Spacer(Modifier.width(4.dp))

                            Text(
                                text = it,
                                style = NapzakMarketTheme.typography.caption10sb,
                                color = NapzakMarketTheme.colors.red,
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = NapzakMarketTheme.colors.red,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    Text(
                        text = genreName,
                        style = NapzakMarketTheme.typography.title20b.copy(
                            color = NapzakMarketTheme.colors.gray500,
                        ),
                        modifier = Modifier.padding(start = 20.dp, bottom = 14.dp),
                    )
                }
            }
        }

        stickyHeader {
            TradeTypeTabBar(
                selectedTab = selectedTab,
                onTabClicked = onTabClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = NapzakMarketTheme.colors.white)
                    .padding(horizontal = 20.dp),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = NapzakMarketTheme.colors.gray10)
                    .padding(horizontal = 20.dp)
                    .padding(top = 15.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                if (selectedTab == TradeType.SELL) {
                    BasicFilterChip(
                        filterName = stringResource(explore_unopened),
                        isClicked = isUnopenSelected,
                        onChipClick = onUnopenFilterClick,
                    )
                }

                BasicFilterChip(
                    filterName = stringResource(explore_exclude_sold_out),
                    isClicked = isSoldOutSelected,
                    onChipClick = onExcludeSoldOutFilterClick,
                )
            }
        }

        item {
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
                            append(stringResource(explore_count, productCount))
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
private fun GenreDetailScreenPreview(modifier: Modifier = Modifier) {
    val genreInfo = GenreInfo(0, "산리오", "지금 핫한", "")

    NapzakMarketTheme {
        GenreDetailSuccessScreen(
            genreInfo = genreInfo,
            selectedTab = TradeType.SELL,
            isUnopenSelected = false,
            isSoldOutSelected = false,
            productCount = 0,
            productList = emptyList(),
            sortType = SortType.RECENT,
            sortBottomSheetState = false,
            onDismissRequest = {},
            onBackButtonClick = {},
            onHomeButtonClick = {},
            onTabClick = {},
            onUnopenFilterClick = {},
            onExcludeSoldOutFilterClick = {},
            onProductClick = {},
            onSortOptionClick = {},
            onSortItemClick = {},
            onLikeButtonClick = { id, value -> },
            modifier = modifier,
        )
    }
}