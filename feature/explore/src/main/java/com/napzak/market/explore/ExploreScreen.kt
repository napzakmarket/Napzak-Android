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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.BottomSheetType
import com.napzak.market.common.type.SortType
import com.napzak.market.common.type.TradeStatusType
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.R.drawable.ic_gray_arrow_down
import com.napzak.market.designsystem.R.drawable.ic_no_searching_result
import com.napzak.market.designsystem.R.string.heart_click_snackbar_message
import com.napzak.market.designsystem.component.GenreFilterChip
import com.napzak.market.designsystem.component.loading.NapzakLoadingOverlay
import com.napzak.market.designsystem.component.productItem.NapzakLargeProductItem
import com.napzak.market.designsystem.component.tabbar.TradeTypeTabBar
import com.napzak.market.designsystem.component.textfield.SearchTextField
import com.napzak.market.designsystem.component.toast.LocalNapzakToast
import com.napzak.market.designsystem.component.toast.ToastType
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.explore.component.BasicFilterChip
import com.napzak.market.explore.component.ExploreBottomSheetScreen
import com.napzak.market.explore.component.GenreNavigationButton
import com.napzak.market.explore.state.ExploreBottomSheetState
import com.napzak.market.explore.state.ExploreProducts
import com.napzak.market.explore.state.ExploreUiState
import com.napzak.market.feature.explore.R.string.explore_count
import com.napzak.market.feature.explore.R.string.explore_empty_search_result_subtitle
import com.napzak.market.feature.explore.R.string.explore_empty_search_result_title
import com.napzak.market.feature.explore.R.string.explore_exclude_sold_out
import com.napzak.market.feature.explore.R.string.explore_product
import com.napzak.market.feature.explore.R.string.explore_search_hint
import com.napzak.market.feature.explore.R.string.explore_unopened
import com.napzak.market.genre.model.Genre
import com.napzak.market.product.model.Product
import com.napzak.market.ui_util.noRippleClickable

@Composable
internal fun ExploreRoute(
    onSearchNavigate: () -> Unit,
    onGenreDetailNavigate: (Long) -> Unit,
    onProductDetailNavigate: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExploreViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val toast = LocalNapzakToast.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val bottomSheetState by viewModel.bottomSheetState.collectAsStateWithLifecycle()

    LaunchedEffect(
        uiState.selectedTab,
        uiState.filteredGenres,
        uiState.isUnopenSelected,
        uiState.isSoldOutSelected,
        uiState.sortOption,
    ) {
        viewModel.updateExploreInformation()
    }

    LaunchedEffect(viewModel.genreSearchTerm) {
        viewModel.updateGenreSearchResult()
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is ExploreSideEffect.ShowHeartToast -> {
                        toast.makeText(
                            toastType = ToastType.HEART,
                            message = context.getString(heart_click_snackbar_message),
                            yOffset = toast.toastOffsetWithBottomBar()
                        )
                    }

                    is ExploreSideEffect.CancelToast -> {
                        toast.cancel()
                    }
                }
            }
    }

    ExploreScreen(
        searchTerm = viewModel.searchTerm,
        uiState = uiState,
        bottomSheetState = bottomSheetState,
        onDismissRequest = viewModel::updateBottomSheetVisibility,
        onSearchNavigate = {
            viewModel.trackSearchOpened()
            onSearchNavigate()
        },
        onTabClick = viewModel::updateTradeType,
        onGenreFilterClick = {
            viewModel.updateBottomSheetVisibility(BottomSheetType.GENRE_SEARCHING)
        },
        onGenreBottomSheetTextChange = viewModel::updateGenreSearchTerm,
        onGenreSelectButtonClick = { newGenres ->
            viewModel.updateSelectedGenres(newGenres)
            viewModel.updateBottomSheetVisibility(BottomSheetType.GENRE_SEARCHING)
        },
        onUnopenFilterClick = viewModel::updateUnopenFilter,
        onExcludeSoldOutFilterClick = viewModel::updateSoldOutFilter,
        onGenreDetailNavigate = onGenreDetailNavigate,
        onSortOptionClick = {
            viewModel.updateBottomSheetVisibility(BottomSheetType.SORT)
        },
        onSortItemClick = { newSortOption ->
            viewModel.updateSortOption(newSortOption)
            viewModel.updateBottomSheetVisibility(BottomSheetType.SORT)
        },
        onProductDetailNavigate = { id, type ->
            viewModel.trackViewedProduct(id, type)
            onProductDetailNavigate(id)
        },
        onLikeButtonClick = { id, value ->
            viewModel.updateProductIsInterested(productId = id, isInterested = value)
        },
        modifier = modifier,
    )
}

@Composable
private fun ExploreScreen(
    searchTerm: String?,
    uiState: ExploreUiState,
    bottomSheetState: ExploreBottomSheetState,
    onDismissRequest: (BottomSheetType) -> Unit,
    onSearchNavigate: () -> Unit,
    onTabClick: (TradeType) -> Unit,
    onGenreFilterClick: () -> Unit,
    onGenreBottomSheetTextChange: (String) -> Unit,
    onGenreSelectButtonClick: (List<Genre>) -> Unit,
    onUnopenFilterClick: () -> Unit,
    onExcludeSoldOutFilterClick: () -> Unit,
    onSortOptionClick: (SortType) -> Unit,
    onSortItemClick: (SortType) -> Unit,
    onGenreDetailNavigate: (Long) -> Unit,
    onProductDetailNavigate: (Long, String) -> Unit,
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
                ExploreSuccessScreen(
                    searchTerm = searchTerm,
                    selectedTab = selectedTab,
                    filteredGenres = filteredGenres,
                    isUnopenSelected = isUnopenSelected,
                    isSoldOutSelected = isSoldOutSelected,
                    sortOption = sortOption,
                    genreItems = genreSearchResultItems,
                    products = uiState.loadState.data.productList,
                    productCount = uiState.loadState.data.productCount,
                    bottomSheetState = bottomSheetState,
                    onSearchNavigate = onSearchNavigate,
                    onTabClick = onTabClick,
                    onGenreFilterClick = onGenreFilterClick,
                    onDismissRequest = onDismissRequest,
                    onGenreBottomSheetTextChange = onGenreBottomSheetTextChange,
                    onGenreSelectButtonClick = onGenreSelectButtonClick,
                    onUnopenFilterClick = onUnopenFilterClick,
                    onExcludeSoldOutFilterClick = onExcludeSoldOutFilterClick,
                    onSortOptionClick = onSortOptionClick,
                    onSortItemClick = onSortItemClick,
                    onGenreDetailNavigate = onGenreDetailNavigate,
                    onProductDetailNavigate = onProductDetailNavigate,
                    onLikeButtonClick = onLikeButtonClick,
                    modifier = modifier,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExploreSuccessScreen(
    searchTerm: String?,
    selectedTab: TradeType,
    filteredGenres: List<Genre>,
    isUnopenSelected: Boolean,
    isSoldOutSelected: Boolean,
    sortOption: SortType,
    genreItems: List<Genre>,
    productCount: Int,
    products: List<Product>,
    bottomSheetState: ExploreBottomSheetState,
    onSearchNavigate: () -> Unit,
    onTabClick: (TradeType) -> Unit,
    onGenreFilterClick: () -> Unit,
    onDismissRequest: (BottomSheetType) -> Unit,
    onGenreBottomSheetTextChange: (String) -> Unit,
    onGenreSelectButtonClick: (List<Genre>) -> Unit,
    onUnopenFilterClick: () -> Unit,
    onExcludeSoldOutFilterClick: () -> Unit,
    onSortOptionClick: (SortType) -> Unit,
    onSortItemClick: (SortType) -> Unit,
    onGenreDetailNavigate: (Long) -> Unit,
    onProductDetailNavigate: (Long, String) -> Unit,
    onLikeButtonClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val listState = rememberLazyListState()
    var lastSelectedTab by remember { mutableStateOf(selectedTab) }

    LaunchedEffect(selectedTab) {
        if (lastSelectedTab!= selectedTab) {
            lastSelectedTab = selectedTab
            listState.animateScrollToItem(0)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NapzakMarketTheme.colors.white),
    ) {
        ExploreSearchTextField(
            searchTerm = searchTerm ?: "",
            modifier = Modifier
                .padding(top = 30.dp)
                .padding(horizontal = 20.dp)
                .noRippleClickable(onSearchNavigate),
        )

        Spacer(Modifier.height(20.dp))

        TradeTypeTabBar(
            selectedTab = selectedTab,
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
                genreList = filteredGenres,
                onChipClick = onGenreFilterClick,
            )

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

        GenreAndProductList(
            searchTerm = searchTerm,
            genreList = filteredGenres,
            productListState = listState,
            productCount = productCount,
            productList = products,
            sortType = sortOption,
            onGenreButtonClick = onGenreDetailNavigate,
            onSortOptionClick = { onSortOptionClick(sortOption) },
            onProductClick = onProductDetailNavigate,
            onLikeButtonClick = onLikeButtonClick,
        )

        if (!searchTerm.isNullOrEmpty() && productCount==0) {
            EmptySearchResultView()
        }
    }

    ExploreBottomSheetScreen(
        exploreBottomSheetState = bottomSheetState,
        sheetState = sheetState,
        selectedGenres = filteredGenres,
        genreItems = genreItems,
        sortType = sortOption,
        onDismissRequest = onDismissRequest,
        onSortItemClick = onSortItemClick,
        onTextChange = onGenreBottomSheetTextChange,
        onGenreSelectButtonClick = onGenreSelectButtonClick,
    )
}

@Composable
private fun EmptySearchResultView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row {
            Spacer(Modifier.weight(1.5f))
            Icon(
                imageVector = ImageVector.vectorResource(ic_no_searching_result),
                contentDescription = null,
                tint = Color.Unspecified,
            )
            Spacer(Modifier.weight(1f))
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = stringResource(explore_empty_search_result_title),
            style = NapzakMarketTheme.typography.body16sb.copy(
                color = NapzakMarketTheme.colors.gray300,
            )
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = stringResource(explore_empty_search_result_subtitle),
            style = NapzakMarketTheme.typography.caption12sb.copy(
                color = NapzakMarketTheme.colors.gray200,
            )
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
        enabled = false,
    )
}

@Composable
private fun GenreAndProductList(
    searchTerm: String?,
    genreList: List<Genre>,
    productListState: LazyListState,
    productCount: Int,
    productList: List<Product>,
    sortType: SortType,
    onGenreButtonClick: (Long) -> Unit,
    onSortOptionClick: () -> Unit,
    onProductClick: (Long, String) -> Unit,
    onLikeButtonClick: (Long, Boolean) -> Unit,
) {
    LazyColumn(
        state = productListState,
        modifier = Modifier
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
            }
        }

        if (productList.isNotEmpty() || searchTerm.isNullOrEmpty()) {
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
                                imageVector = ImageVector.vectorResource(ic_gray_arrow_down),
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
                                    .noRippleClickable { onProductClick(productId, tradeType) },
                            )
                        }
                    }

                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}

@Preview
@Composable
private fun ExploreScreenPreview(modifier: Modifier = Modifier) {
    NapzakMarketTheme {
        ExploreScreen(
            searchTerm = "",
            uiState = ExploreUiState(loadState = UiState.Success(ExploreProducts(1, emptyList()))),
            bottomSheetState = ExploreBottomSheetState(),
            onDismissRequest = { },
            onSearchNavigate = { },
            onTabClick = { },
            onGenreFilterClick = { },
            onGenreSelectButtonClick = { },
            onGenreBottomSheetTextChange = { },
            onUnopenFilterClick = { },
            onExcludeSoldOutFilterClick = { },
            onSortOptionClick = { },
            onSortItemClick = { },
            onGenreDetailNavigate = { },
            onProductDetailNavigate = { _, _ -> },
            onLikeButtonClick = { id, value -> },
            modifier = modifier
        )
    }
}
