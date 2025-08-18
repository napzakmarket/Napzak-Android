package com.napzak.market.store.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.BottomSheetType
import com.napzak.market.common.type.MarketTab
import com.napzak.market.common.type.SortType
import com.napzak.market.designsystem.R.string.heart_click_snackbar_message
import com.napzak.market.designsystem.component.loading.NapzakLoadingOverlay
import com.napzak.market.designsystem.component.toast.LocalNapzakToast
import com.napzak.market.designsystem.component.toast.ToastType
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.genre.model.Genre
import com.napzak.market.product.model.Product
import com.napzak.market.store.model.StoreDetail
import com.napzak.market.store.store.component.StoreBottomSheetScreen
import com.napzak.market.store.store.component.StoreScrollSection
import com.napzak.market.store.store.component.StoreTopBar
import com.napzak.market.store.store.state.StoreBottomSheetState
import com.napzak.market.store.store.state.StoreOptionState
import com.napzak.market.store.store.state.StoreUiState

@Composable
internal fun StoreRoute(
    onNavigateUp: () -> Unit,
    onProfileEditNavigate: (Long) -> Unit,
    onProductDetailNavigate: (Long) -> Unit,
    onStoreReportNavigate: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StoreViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val toast = LocalNapzakToast.current
    val context = LocalContext.current

    val uiState by viewModel.storeUiState.collectAsStateWithLifecycle()
    val storeOptionState by viewModel.storeOptionState.collectAsStateWithLifecycle()
    val bottomSheetState by viewModel.bottomSheetState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getStoreInformation()
        viewModel.updateGenreItemsInBottomSheet()
        viewModel.updateGenreSearchResult()
    }

    LaunchedEffect(storeOptionState) {
        viewModel.updateStoreProducts()
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is StoreSideEffect.ShowHeartToast -> {
                        toast.makeText(
                            toastType = ToastType.HEART,
                            message = context.getString(heart_click_snackbar_message),
                            yOffset = 20,
                        )
                    }

                    is StoreSideEffect.CancelToast -> {
                        toast.cancel()
                    }
                }
            }
    }

    StoreScreen(
        uiState = uiState,
        storeOptionState = storeOptionState,
        bottomSheetState = bottomSheetState,
        onDismissRequest = viewModel::updateBottomSheetVisibility,
        onBackButtonClick = onNavigateUp,
        onMenuButtonClick = {
            viewModel.updateBottomSheetVisibility(BottomSheetType.STORE_REPORT)
        },
        onProfileEditClick = { onProfileEditNavigate(viewModel.storeId) },
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
            viewModel.updateProductIsInterested(productId = id, isInterested = value)
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
    storeOptionState: StoreOptionState,
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
    when (uiState.isLoaded) {
        is UiState.Loading -> NapzakLoadingOverlay()

        is UiState.Empty -> {}

        is UiState.Failure -> {}

        is UiState.Success -> {
            with(storeOptionState) {
                StoreSuccessScreen(
                    storeDetail = (uiState.storeDetailState as UiState.Success<StoreDetail>).data,
                    selectedTab = selectedTab,
                    filteredGenres = filteredGenres,
                    isOnSale = isOnSale,
                    sortType = sortOption,
                    genreItems = genreSearchResultItems,
                    productCount = (uiState.storeProductsState as UiState.Success<Pair<Int, List<Product>>>).data.first,
                    productList = uiState.storeProductsState.data.second,
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
    productCount: Int,
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
            .systemBarsPadding()
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
            productCount = productCount,
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
            productCount = 0,
            productList = emptyList(),
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