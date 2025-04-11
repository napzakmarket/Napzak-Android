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
import com.napzak.market.detail.model.ProductDetail
import com.napzak.market.detail.model.ProductPhoto
import com.napzak.market.detail.model.StoreInfo
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
    val isInterested by viewModel.isInterested.collectAsStateWithLifecycle()

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
        productDetail = viewModel.productDetail,
        productPhotos = viewModel.productPhotos.toImmutableList(),
        marketInfo = viewModel.marketInfo,
        isInterested = isInterested,
        onMarketClick = { onMarketNavigate(viewModel.marketInfo.userId) },
        onChatButtonClick = { onChatNavigate(viewModel.productDetail.productId) },
        onLikeButtonClick = { viewModel.updateIsInterested(!isInterested) },
        onBackButtonClick = onNavigateUp,
        onModifyProductClick = { onModifyNavigate(viewModel.productDetail.productId) },
        onDeleteProductClick = viewModel::deleteProduct,
        onReportProductClick = { onReportNavigate(viewModel.productDetail.productId) },
        modifier = modifier,
    )
}

@Composable
fun ProductDetailScreen(
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
            modifier = Modifier.padding(innerPadding)
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

        ProductDetailScreen(
            isInterested = isLiked,
            onLikeButtonClick = { isLiked = !isLiked },
            productDetail = ProductDetail.mock,
            productPhotos = ProductPhoto.mockList.toImmutableList(),
            marketInfo = StoreInfo.mock,
            onMarketClick = {},
            onChatButtonClick = {},
            onBackButtonClick = {},
            onModifyProductClick = {},
            onDeleteProductClick = {},
            onReportProductClick = {},
        )
    }
}
