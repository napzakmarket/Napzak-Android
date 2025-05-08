package com.napzak.market.store.store

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.BottomSheetType
import com.napzak.market.common.type.MarketTab
import com.napzak.market.common.type.SortType
import com.napzak.market.common.type.TradeStatusType
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.component.bottomsheet.Genre
import com.napzak.market.designsystem.component.productItem.NapzakLargeProductItem
import com.napzak.market.designsystem.component.tabbar.MarketTabBar
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.store.R.drawable.ic_down_chevron_7
import com.napzak.market.feature.store.R.drawable.ic_kebap
import com.napzak.market.feature.store.R.drawable.ic_left_chevron
import com.napzak.market.feature.store.R.string.back_button_description
import com.napzak.market.feature.store.R.string.store_count
import com.napzak.market.feature.store.R.string.store_edit_profile
import com.napzak.market.feature.store.R.string.store_empty_text
import com.napzak.market.feature.store.R.string.store_filter_buying
import com.napzak.market.feature.store.R.string.store_filter_selling
import com.napzak.market.feature.store.R.string.store_product
import com.napzak.market.store.component.BasicFilterChip
import com.napzak.market.store.component.GenreChip
import com.napzak.market.store.component.GenreFilterChip
import com.napzak.market.store.component.StoreBottomSheetScreen
import com.napzak.market.store.model.Product
import com.napzak.market.store.model.StoreDetail
import com.napzak.market.store.store.state.StoreBottomSheetState
import com.napzak.market.store.store.state.StoreUiState
import com.napzak.market.util.android.noRippleClickable

@Composable
internal fun StoreRoute(
    onNavigateUp: () -> Unit,
    onProfileEditNavigate: () -> Unit,
    onProductDetailNavigate: (Long) -> Unit,
    onStoreReportNavigate: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StoreViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val bottomSheetState by viewModel.bottomSheetState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.updateStoreInformation()
        viewModel.updateGenreItemsInBottomSheet()
    }

    LaunchedEffect(
        uiState.selectedTab,
        uiState.filteredGenres,
        uiState.isOnSale,
        uiState.sortOption,
    ) {
        viewModel.updateStoreProducts()
    }

    StoreScreen(
        uiState = uiState,
        bottomSheetState = bottomSheetState,
        onDismissRequest = viewModel::updateBottomSheetVisibility,
        onBackButtonClick = onNavigateUp,
        onMenuButtonClick = {
            viewModel.updateBottomSheetVisibility(BottomSheetType.STORE_REPORT)
        },
        onProfileEditClick = onProfileEditNavigate,
        onTabClicked = viewModel::updateMarketTabType,
        onGenreFilterClick = {
            viewModel.updateBottomSheetVisibility(BottomSheetType.GENRE_SEARCHING)
        },
        onGenreBottomSheetTextChange = viewModel::updateGenreSearchTerm,
        onGenreSelectButtonClick = { newGenres ->
            viewModel.updateSelectedGenres(newGenres)
            viewModel.updateBottomSheetVisibility(BottomSheetType.GENRE_SEARCHING)
        },
        onFilterClick = viewModel::updateIsOnSale,
        onSortOptionClick = {
            viewModel.updateBottomSheetVisibility(BottomSheetType.SORT)
        },
        onSortItemClick = { newSortOption ->
            viewModel.updateSortOption(newSortOption)
            viewModel.updateBottomSheetVisibility(BottomSheetType.SORT)
        },
        onProductItemClick = onProductDetailNavigate,
        onLikeButtonClick = { id, value ->
            viewModel.updateProductIsInterested(productId = id, isLiked = value)
        },
        onStoreReportButtonClick = {
            viewModel.updateBottomSheetVisibility(BottomSheetType.STORE_REPORT)
            onStoreReportNavigate(viewModel.storeId)
        },
        modifier = modifier,
    )
}

@Composable
private fun StoreScreen(
    uiState: StoreUiState,
    bottomSheetState: StoreBottomSheetState,
    onBackButtonClick: () -> Unit,
    onMenuButtonClick: () -> Unit,
    onProfileEditClick: () -> Unit,
    onTabClicked: (MarketTab) -> Unit,
    onGenreFilterClick: () -> Unit,
    onDismissRequest: (BottomSheetType) -> Unit,
    onGenreBottomSheetTextChange: (String) -> Unit,
    onGenreSelectButtonClick: (List<Genre>) -> Unit,
    onFilterClick: () -> Unit,
    onSortOptionClick: (SortType) -> Unit,
    onSortItemClick: (SortType) -> Unit,
    onProductItemClick: (Long) -> Unit,
    onLikeButtonClick: (Long, Boolean) -> Unit,
    onStoreReportButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState.loadState) {
        is UiState.Loading -> {
        }

        is UiState.Empty -> {
        }

        is UiState.Failure -> {
        }

        is UiState.Success -> {
            with(uiState) {
                StoreSuccessScreen(
                    storeDetail = uiState.loadState.data.storeDetail,
                    selectedTab = selectedTab,
                    filteredGenres = filteredGenres,
                    isOnSale = isOnSale,
                    sortType = sortOption,
                    genreItems = genreSearchResultItems,
                    productList = uiState.loadState.data.productList,
                    bottomSheetState = bottomSheetState,
                    onProfileEditClick = onProfileEditClick,
                    onTabClicked = onTabClicked,
                    onGenreFilterClick = onGenreFilterClick,
                    onDismissRequest = onDismissRequest,
                    onGenreBottomSheetTextChange = onGenreBottomSheetTextChange,
                    onGenreSelectButtonClick = onGenreSelectButtonClick,
                    onFilterClick = onFilterClick,
                    onBackButtonClick = onBackButtonClick,
                    onSortOptionClick = onSortOptionClick,
                    onSortItemClick = onSortItemClick,
                    onProductItemClick = onProductItemClick,
                    onLikeButtonClick = onLikeButtonClick,
                    onMenuButtonClick = onMenuButtonClick,
                    onStoreReportButtonClick = onStoreReportButtonClick,
                    modifier = modifier,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StoreSuccessScreen(
    storeDetail: StoreDetail,
    selectedTab: MarketTab,
    filteredGenres: List<Genre>,
    isOnSale: Boolean,
    sortType: SortType,
    genreItems: List<Genre>,
    productList: List<Product>,
    bottomSheetState: StoreBottomSheetState,
    onProfileEditClick: () -> Unit,
    onTabClicked: (MarketTab) -> Unit,
    onGenreFilterClick: () -> Unit,
    onDismissRequest: (BottomSheetType) -> Unit,
    onGenreBottomSheetTextChange: (String) -> Unit,
    onGenreSelectButtonClick: (List<Genre>) -> Unit,
    onFilterClick: () -> Unit,
    onBackButtonClick: () -> Unit,
    onSortOptionClick: (SortType) -> Unit,
    onSortItemClick: (SortType) -> Unit,
    onProductItemClick: (Long) -> Unit,
    onLikeButtonClick: (Long, Boolean) -> Unit,
    onMenuButtonClick: () -> Unit,
    onStoreReportButtonClick: () -> Unit,
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
        StoreTopBar(
            isOwner = storeDetail.isOwner,
            onBackButtonClick = onBackButtonClick,
            onMenuButtonClick = onMenuButtonClick
        )

        StoreScrollSection(
            storeDetail = storeDetail,
            isOwner = storeDetail.isOwner,
            selectedTab = selectedTab,
            filteredGenres = filteredGenres,
            isOnSale = isOnSale,
            sortType = sortType,
            productList = productList,
            onProfileEditClick = onProfileEditClick,
            onTabClicked = onTabClicked,
            onGenreFilterClick = onGenreFilterClick,
            onFilterClick = onFilterClick,
            onSortOptionClick = onSortOptionClick,
            onProductDetailNavigate = onProductItemClick,
            onLikeButtonClick = onLikeButtonClick,
        )
    }

    StoreBottomSheetScreen(
        storeBottomSheetState = bottomSheetState,
        sheetState = sheetState,
        selectedGenres = filteredGenres,
        genreItems = genreItems,
        sortType = sortType,
        onDismissRequest = onDismissRequest,
        onSortItemClick = onSortItemClick,
        onTextChange = onGenreBottomSheetTextChange,
        onGenreSelectButtonClick = onGenreSelectButtonClick,
        onStoreReportButtonClick = onStoreReportButtonClick,
    )
}

@Composable
private fun StoreTopBar(
    isOwner: Boolean,
    onBackButtonClick: () -> Unit,
    onMenuButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(start = 20.dp, top = 62.dp, end = 20.dp, bottom = 22.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(ic_left_chevron),
            contentDescription = stringResource(back_button_description),
            tint = NapzakMarketTheme.colors.gray200,
            modifier = Modifier.noRippleClickable(onBackButtonClick),
        )

        Spacer(Modifier.weight(1f))

        if (!isOwner) {
            Icon(
                imageVector = ImageVector.vectorResource(ic_kebap),
                contentDescription = null,
                tint = NapzakMarketTheme.colors.gray200,
                modifier = Modifier.noRippleClickable(onMenuButtonClick),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun StoreScrollSection(
    storeDetail: StoreDetail,
    isOwner: Boolean,
    selectedTab: MarketTab,
    filteredGenres: List<Genre>,
    isOnSale: Boolean,
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
                        isClicked = isOnSale,
                        onChipClick = onFilterClick,
                    )
                }
            }
        }

        if (selectedTab != MarketTab.REVIEW) {
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
}

@Composable
private fun StoreInfoSection(
    storeDetail: StoreDetail,
    isMyStore: Boolean,
    onProfileEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    with(storeDetail) {
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
                            .data(coverUrl)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2.25f)
                            .background(color = NapzakMarketTheme.colors.gray100),
                    )

                    if (isMyStore) {
                        Box(
                            modifier = Modifier
                                .padding(end = 20.dp, bottom = 10.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = stringResource(store_edit_profile),
                                style = NapzakMarketTheme.typography.caption10sb,
                                color = NapzakMarketTheme.colors.white,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .noRippleClickable(onProfileEditClick)
                                    .background(
                                        color = NapzakMarketTheme.colors.transBlack,
                                        shape = RoundedCornerShape(24.dp),
                                    )
                                    .padding(6.dp),
                            )
                        }
                    }
                }

                Spacer(Modifier.height(35.dp))
            }

            AsyncImage(
                model = ImageRequest
                    .Builder(context)
                    .data(photoUrl)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .background(
                        color = NapzakMarketTheme.colors.gray200,
                        shape = RoundedCornerShape(50.dp),
                    )
                    .border(
                        width = 5.dp,
                        color = NapzakMarketTheme.colors.white,
                        shape = RoundedCornerShape(50.dp),
                    ),
            )
        }

        Spacer(Modifier.height(9.dp))

        Text(
            text = nickname,
            style = NapzakMarketTheme.typography.body16sb,
            color = NapzakMarketTheme.colors.gray500,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = description,
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
            horizontalArrangement = Arrangement.spacedBy(5.dp),
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
    NapzakMarketTheme {
        StoreSuccessScreen(
            selectedTab = MarketTab.SELL,
            storeDetail = StoreDetail.mockStoreInfo,
            filteredGenres = emptyList(),
            isOnSale = false,
            sortType = SortType.RECENT,
            genreItems = emptyList(),
            productList = Product.mockMixedProduct,
            bottomSheetState = StoreBottomSheetState(),
            onBackButtonClick = {},
            onMenuButtonClick = {},
            onProfileEditClick = {},
            onTabClicked = {},
            onGenreFilterClick = {},
            onDismissRequest = {},
            onGenreBottomSheetTextChange = {},
            onGenreSelectButtonClick = {},
            onFilterClick = {},
            onSortOptionClick = {},
            onSortItemClick = {},
            onProductItemClick = {},
            onLikeButtonClick = { id, value -> },
            onStoreReportButtonClick = {},
            modifier = modifier,
        )
    }
}