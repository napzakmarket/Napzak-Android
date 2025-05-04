package com.napzak.market.detail

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.ProductConditionType
import com.napzak.market.common.type.TradeStatusType
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.R.string.heart_click_snackbar_message
import com.napzak.market.designsystem.component.HeartClickSnackBar
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.detail.component.bottombar.ProductDetailBottomBar
import com.napzak.market.detail.component.bottomsheet.MyProductBottomSheet
import com.napzak.market.detail.component.bottomsheet.ProductBottomSheet
import com.napzak.market.detail.component.divider.SectionDivider
import com.napzak.market.detail.component.group.ProductImageGroup
import com.napzak.market.detail.component.group.ProductInformationBuyGroup
import com.napzak.market.detail.component.group.ProductInformationGroup
import com.napzak.market.detail.component.group.ProductInformationSellGroup
import com.napzak.market.detail.component.group.ProductMarketGroup
import com.napzak.market.detail.component.topbar.DetailTopBar
import com.napzak.market.product.model.ProductDetail
import com.napzak.market.product.model.ProductDetail.ProductPhoto
import com.napzak.market.product.model.ProductDetail.StoreInfo
import com.napzak.market.util.common.formatToPriceString
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@Composable
internal fun ProductDetailRoute(
    onMarketNavigate: (userId: Long) -> Unit,
    onChatNavigate: (productId: Long) -> Unit,
    onModifyNavigate: (productId: Long) -> Unit,
    onReportNavigate: (productId: Long) -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductDetailViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.productDetail.collectAsStateWithLifecycle()
    val isInterested by viewModel.isInterested.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getProductDetail()
    }

    LaunchedEffect(viewModel) {
        viewModel.debounceIsInterested()
    }

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycle = lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    ProductDetailSideEffect.NavigateUp -> {
                        onNavigateUp()
                    }
                }
            }
    }

    ProductDetailScreen(
        uiState = uiState,
        isInterested = isInterested,
        onMarketClick = { onMarketNavigate(viewModel.marketInfo.userId) },
        onChatButtonClick = onChatNavigate,
        onLikeButtonClick = { viewModel.updateIsInterested(!isInterested) },
        onBackButtonClick = onNavigateUp,
        onModifyProductClick = onModifyNavigate,
        onDeleteProductClick = viewModel::deleteProduct,
        onReportProductClick = onReportNavigate,
        modifier = modifier,
    )
}

@Composable
fun ProductDetailScreen(
    uiState: UiState<ProductDetail>,
    isInterested: Boolean,
    onMarketClick: () -> Unit,
    onChatButtonClick: (productId: Long) -> Unit,
    onLikeButtonClick: (productId: Long) -> Unit,
    onBackButtonClick: () -> Unit,
    onModifyProductClick: (productId: Long) -> Unit,
    onDeleteProductClick: (productId: Long) -> Unit,
    onReportProductClick: (productId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is UiState.Success -> {
            val productDetail = uiState.data
            val productPhotos = uiState.data.productPhotos
            val storeInfo = uiState.data.storeInfo

            ProductDetailSuccessScreen(
                productDetail = productDetail,
                productPhotos = productPhotos.toImmutableList(),
                marketInfo = storeInfo,
                isInterested = isInterested,
                onMarketClick = onMarketClick,
                onChatButtonClick = { onChatButtonClick(productDetail.productId) },
                onLikeButtonClick = { onLikeButtonClick(productDetail.productId) },
                onBackButtonClick = onBackButtonClick,
                onModifyProductClick = { onModifyProductClick(productDetail.productId) },
                onDeleteProductClick = { onDeleteProductClick(productDetail.productId) },
                onReportProductClick = { onReportProductClick(productDetail.productId) },
                modifier = modifier,
            )
        }

        is UiState.Failure -> {}
        is UiState.Empty -> {}
        is UiState.Loading -> {}
    }
}

@Composable
private fun ProductDetailSuccessScreen(
    productDetail: ProductDetail,
    productPhotos: ImmutableList<ProductPhoto>,
    marketInfo: StoreInfo,
    isInterested: Boolean,
    onMarketClick: () -> Unit,
    onChatButtonClick: () -> Unit,
    onLikeButtonClick: () -> Unit,
    onBackButtonClick: () -> Unit,
    onModifyProductClick: () -> Unit,
    onDeleteProductClick: () -> Unit,
    onReportProductClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tradeType = remember(productDetail) { TradeType.fromName(productDetail.tradeType) }
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    var tradeStatus by remember { mutableStateOf(TradeStatusType.valueOf(productDetail.tradeStatus)) }
    var sheetVisibility by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DetailTopBar(
                onBackClick = onBackButtonClick,
                onOptionClick = { sheetVisibility = true },
            )
        },
        bottomBar = {
            ProductDetailBottomBar(
                isLiked = isInterested,
                onChatButtonClick = onChatButtonClick,
                onLikeButtonClick = {
                    onLikeButtonClick()
                    coroutineScope.launch {
                        // debounce 처리로 인해 스낵바 조건이 바뀜
                        if (!isInterested) snackBarHostState.showSnackbar("")
                        else snackBarHostState.currentSnackbarData?.dismiss()
                    }
                },
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) {
                HeartClickSnackBar(message = stringResource(heart_click_snackbar_message))
            }
        },
        containerColor = NapzakMarketTheme.colors.white,
        modifier = modifier,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
        ) {
            item {
                ProductImageGroup(
                    imageUrls = productPhotos.map { it.photoUrl }.toImmutableList(),
                    contentDescription = productDetail.productName,
                    tradeStatusType = tradeStatus,
                )

                with(productDetail) {
                    ProductInformationGroup(
                        tradeType = tradeType,
                        isPriceNegotiable = isPriceNegotiable,
                        commentCount = chatCount,
                        likeCount = interestCount,
                        genre = genreName,
                        title = productName,
                        price = price.toString().formatToPriceString(),
                        updatedDate = uploadTime,
                        description = description,
                        modifier = Modifier,
                    )

                    SectionDivider()

                    when (tradeType) {
                        TradeType.BUY -> {
                            ProductInformationBuyGroup(
                                productCondition = ProductConditionType.fromConditionByName(
                                    productCondition
                                ),
                                isDeliveryIncluded = isDeliveryIncluded,
                                standardDeliveryFee = standardDeliveryFee,
                                halfDeliveryFee = halfDeliveryFee,
                            )
                        }

                        TradeType.SELL -> {
                            ProductInformationSellGroup(
                                isPriceNegotiable = isPriceNegotiable,
                            )
                        }
                    }
                }

                SectionDivider()

                with(marketInfo) {
                    ProductMarketGroup(
                        marketImage = storePhoto,
                        marketName = nickname,
                        sellCount = totalSellCount.toString(),
                        buyCount = totalBuyCount.toString(),
                        onMarketProfileClick = onMarketClick,
                    )
                }
            }
        }
        if (sheetVisibility) {
            if (productDetail.isOwnedByCurrentUser) {
                MyProductBottomSheet(
                    tradeType = tradeType,
                    tradeStatus = tradeStatus,
                    onDismissRequest = { sheetVisibility = false },
                    onModifyClick = onModifyProductClick,
                    onStatusChange = { tradeStatus = it },
                    onDeleteClick = onDeleteProductClick,
                )
            } else {
                ProductBottomSheet(
                    onReportClick = onReportProductClick,
                    onDismissRequest = { sheetVisibility = false },
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 780, widthDp = 360)
@Composable
private fun ProductDetailScreenPreview() {
    NapzakMarketTheme {
        var isLiked by remember { mutableStateOf(false) }

        ProductDetailSuccessScreen(
            isInterested = isLiked,
            onLikeButtonClick = { isLiked = !isLiked },
            productDetail = ProductDetail(
                productId = 1,
                tradeType = "SELL",
                genreName = "은혼",
                productName = "은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩",
                price = 125000,
                uploadTime = "1일전",
                chatCount = 1000,
                interestCount = 1000,
                description = "은혼 긴토키 히지카타 룩업",
                productCondition = "LIKE_NEW",
                standardDeliveryFee = 3600,
                halfDeliveryFee = 1800,
                isDeliveryIncluded = false,
                isPriceNegotiable = true,
                tradeStatus = "BEFORE_TRADE",
                isOwnedByCurrentUser = true,
                isInterested = false,
                productPhotos = emptyList(),
                storeInfo = StoreInfo(
                    userId = 1,
                    storePhoto = "",
                    nickname = "닉네임",
                    totalSellCount = 1000,
                    totalBuyCount = 1000,
                )
            ),
            productPhotos = emptyList<ProductPhoto>().toImmutableList(),
            marketInfo = StoreInfo(
                userId = 1,
                storePhoto = "",
                nickname = "닉네임",
                totalSellCount = 1000,
                totalBuyCount = 1000,
            ),
            onMarketClick = {},
            onChatButtonClick = {},
            onBackButtonClick = {},
            onModifyProductClick = {},
            onDeleteProductClick = {},
            onReportProductClick = {},
        )
    }
}
