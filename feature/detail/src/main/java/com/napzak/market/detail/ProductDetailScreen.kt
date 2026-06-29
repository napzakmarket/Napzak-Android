package com.napzak.market.detail

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.ProductConditionType
import com.napzak.market.common.type.TradeStatusType
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.R.drawable.ic_thick_arrow
import com.napzak.market.designsystem.R.drawable.img_not_found
import com.napzak.market.designsystem.component.button.NapzakButton
import com.napzak.market.designsystem.component.dialog.NapzakDialog
import com.napzak.market.designsystem.component.dialog.NapzakDialogDefault
import com.napzak.market.designsystem.component.image.ZoomableImageScreen
import com.napzak.market.designsystem.component.loading.NapzakLoadingOverlay
import com.napzak.market.designsystem.component.popup.NapzakPhoneVerifyModal
import com.napzak.market.designsystem.component.toast.LocalNapzakToast
import com.napzak.market.designsystem.component.toast.ToastType
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
import com.napzak.market.feature.detail.R.string.detail_deleted_product_button
import com.napzak.market.feature.detail.R.string.detail_deleted_product_subtitle
import com.napzak.market.feature.detail.R.string.detail_deleted_product_title
import com.napzak.market.feature.detail.R.string.detail_dialog_delete_sub_title
import com.napzak.market.feature.detail.R.string.detail_dialog_delete_title
import com.napzak.market.product.model.ProductDetail
import com.napzak.market.product.model.ProductDetail.ProductPhoto
import com.napzak.market.product.model.ProductDetail.StoreInfo
import com.napzak.market.ui_util.formatToPriceString
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun ProductDetailRoute(
    onMarketNavigate: (userId: Long) -> Unit,
    onChatNavigate: (productId: Long) -> Unit,
    onModifyNavigate: (productId: Long, tradeType: TradeType) -> Unit,
    onReportNavigate: (productId: Long) -> Unit,
    onPhoneVerificationNavigate: () -> Unit,
    onNavigateUp: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductDetailViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val toast = LocalNapzakToast.current
    val density = LocalDensity.current
    val statusBars = WindowInsets.statusBars
    val lockedStatusBarTop = remember { statusBars.getTop(density) }

    val uiState by viewModel.productDetail.collectAsStateWithLifecycle()
    val showProductStatusToolTip by viewModel.showProductStatusTooltip.collectAsStateWithLifecycle()
    val isPhoneVerified by viewModel.isPhoneVerified.collectAsStateWithLifecycle()

    var isPhoneVerifyModalVisible by remember { mutableStateOf(false) }

    if (uiState is UiState.Empty) {
        DeletedProductScreen(
            onHomeClick = onNavigateToHome,
            modifier = modifier.systemBarsPadding(),
        )
        return
    }

    LaunchedEffect(Unit) {
        viewModel.checkPhoneVerification()
    }

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycle = lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    ProductDetailSideEffect.NavigateUp -> {
                        onNavigateUp()
                    }

                    is ProductDetailSideEffect.ShowToast -> {
                        with(sideEffect.productDetailToastType) {
                            val yOffset =
                                if (this.toastType == ToastType.HEART)
                                    toast.toastOffsetWithBottomBar()
                                else 100

                            toast.makeText(
                                toastType = this.toastType,
                                fontType = this.fontType,
                                message = context.getString(this.stringRes, sideEffect.message),
                                icon = this.iconRes,
                                yOffset = yOffset,
                            )
                        }
                    }

                    is ProductDetailSideEffect.CancelToast -> {
                        toast.cancel()
                    }

                    is ProductDetailSideEffect.ShareProduct -> {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, sideEffect.url)
                        }
                        context.startActivity(
                            Intent.createChooser(intent, null)
                        )
                    }
                }
            }
    }

    ProductDetailScreen(
        uiState = uiState,
        isPhoneVerifyModalVisible = isPhoneVerifyModalVisible,
        onShareClick = viewModel::onShareClick,
        onMarketClick = onMarketNavigate,
        onChatButtonClick = {
            if (!isPhoneVerified) {
                isPhoneVerifyModalVisible = true
            } else {
                viewModel.trackStartedChat(it)
                onChatNavigate(it)
            }
        },
        onLikeButtonClick = viewModel::updateIsInterested,
        onBackButtonClick = onNavigateUp,
        onModifyProductClick = onModifyNavigate,
        onDeleteProductClick = viewModel::deleteProduct,
        onReportProductClick = { productId ->
            viewModel.trackReportProduct()
            onReportNavigate(productId)
        },
        showProductStatusToolTip = showProductStatusToolTip,
        onTooltipDismiss = viewModel::setShowProductStatusToolTip,
        onTradeStatusChange = viewModel::updateTradeStatus,
        onDismissClick = { isPhoneVerifyModalVisible = false },
        onPhoneVerifyClick = onPhoneVerificationNavigate,
        modifier = modifier
            .padding(top = with(density) { lockedStatusBarTop.toDp() })
            .navigationBarsPadding(),
    )
}

@Composable
private fun ProductDetailScreen(
    uiState: UiState<ProductDetail>,
    showProductStatusToolTip: Boolean,
    isPhoneVerifyModalVisible: Boolean,
    onShareClick: () -> Unit,
    onMarketClick: (userId: Long) -> Unit,
    onChatButtonClick: (productId: Long) -> Unit,
    onLikeButtonClick: (Boolean) -> Unit,
    onBackButtonClick: () -> Unit,
    onModifyProductClick: (productId: Long, tradeType: TradeType) -> Unit,
    onDeleteProductClick: (productId: Long) -> Unit,
    onReportProductClick: (productId: Long) -> Unit,
    onTradeStatusChange: (productId: Long, tradeStatus: String) -> Unit,
    onTooltipDismiss: (Boolean) -> Unit,
    onDismissClick: () -> Unit,
    onPhoneVerifyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var sheetVisibility by remember { mutableStateOf(false) }
    var deleteDialogVisibility by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DetailTopBar(
                showToolTip = showProductStatusToolTip
                        && (uiState is UiState.Success && uiState.data.isOwnedByCurrentUser),
                onBackClick = onBackButtonClick,
                onShareClick = onShareClick,
                onOptionClick = { sheetVisibility = true },
                onTooltipDismiss = { onTooltipDismiss(false) },
            )
        },
        bottomBar = {
            if (uiState is UiState.Success && !uiState.data.isOwnedByCurrentUser) {
                val productId = uiState.data.productId
                val isInterested = uiState.data.isInterested
                ProductDetailBottomBar(
                    isLiked = isInterested,
                    onChatButtonClick = { onChatButtonClick(productId) },
                    onLikeButtonClick = { onLikeButtonClick(!isInterested) },
                )
            }
        },
        containerColor = NapzakMarketTheme.colors.white,
        modifier = modifier,
    ) { innerPadding ->
        when (uiState) {
            is UiState.Success -> {
                val productDetail = uiState.data
                val productPhotos = uiState.data.productPhotos
                val storeInfo = uiState.data.storeInfo

                val tradeType = TradeType.fromName(productDetail.tradeType)
                val tradeStatus = TradeStatusType.get(productDetail.tradeStatus, tradeType)

                SuccessScreen(
                    productDetail = productDetail,
                    productPhotos = productPhotos.toImmutableList(),
                    marketInfo = storeInfo,
                    tradeType = tradeType,
                    tradeStatus = tradeStatus,
                    onMarketClick = { onMarketClick(storeInfo.userId) },
                    modifier = Modifier.padding(innerPadding),
                )

                ProductDetailBottomSheet(
                    sheetVisibility = sheetVisibility,
                    productDetail = productDetail,
                    tradeType = tradeType,
                    tradeStatus = tradeStatus,
                    onModifyProductClick = {
                        onModifyProductClick(
                            productDetail.productId,
                            tradeType
                        )
                    },
                    onDeleteProductClick = { deleteDialogVisibility = true },
                    onReportProductClick = { onReportProductClick(productDetail.productId) },
                    onTradeStatusChange = { newStatus ->
                        onTradeStatusChange(productDetail.productId, newStatus.typeName)
                    },
                    onBottomSheetDismiss = { sheetVisibility = false },
                )

                ProductDetailDeleteDialog(
                    enabled = deleteDialogVisibility,
                    onConfirmClick = { onDeleteProductClick(productDetail.productId) },
                    onDismissClick = { deleteDialogVisibility = false },
                )

                if (isPhoneVerifyModalVisible) {
                    NapzakPhoneVerifyModal(
                        onDismissClick = onDismissClick,
                        onPhoneVerifyClick = onPhoneVerifyClick,
                        modifier = modifier,
                    )
                }
            }

            is UiState.Loading -> NapzakLoadingOverlay()
            else -> {} // TODO: Empty, Failure 처리
        }
    }
}

@Composable
private fun SuccessScreen(
    productDetail: ProductDetail,
    productPhotos: ImmutableList<ProductPhoto>,
    tradeType: TradeType,
    tradeStatus: TradeStatusType,
    marketInfo: StoreInfo,
    onMarketClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedImageIndex: Int? by remember { mutableStateOf(null) }
    val imageUrls = remember(productPhotos) {
        productPhotos.map { it.photoUrl }.toImmutableList()
    }

    BackHandler(selectedImageIndex != null) {
        selectedImageIndex = null
    }

    selectedImageIndex?.let {
        ZoomableImageScreen(
            imageUrls = imageUrls,
            initialPage = it,
            contentDescription = productDetail.productName,
            onBackClick = { selectedImageIndex = null },
        )
    }

    LazyColumn(modifier = modifier) {
        item {
            ProductImageGroup(
                imageUrls = imageUrls,
                contentDescription = productDetail.productName,
                tradeStatusType = tradeStatus,
                onClick = { selectedImageIndex = it },
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
                    TradeType.SELL -> {
                        ProductInformationSellGroup(
                            productCondition = ProductConditionType.fromConditionByName(
                                productCondition
                            ),
                            isDeliveryIncluded = isDeliveryIncluded,
                            standardDeliveryFee = standardDeliveryFee,
                            halfDeliveryFee = halfDeliveryFee,
                        )
                    }

                    TradeType.BUY -> {
                        ProductInformationBuyGroup(
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
}

@Composable
private fun ProductDetailBottomSheet(
    sheetVisibility: Boolean,
    productDetail: ProductDetail,
    tradeType: TradeType,
    tradeStatus: TradeStatusType,
    onModifyProductClick: () -> Unit,
    onDeleteProductClick: () -> Unit,
    onReportProductClick: () -> Unit,
    onTradeStatusChange: (TradeStatusType) -> Unit,
    onBottomSheetDismiss: () -> Unit,
) {
    when {
        sheetVisibility && productDetail.isOwnedByCurrentUser -> {
            MyProductBottomSheet(
                tradeType = tradeType,
                tradeStatus = tradeStatus,
                onDismissRequest = onBottomSheetDismiss,
                onModifyClick = onModifyProductClick,
                onStatusChange = onTradeStatusChange,
                onDeleteClick = onDeleteProductClick,
            )
        }

        sheetVisibility && !productDetail.isOwnedByCurrentUser -> {
            ProductBottomSheet(
                onReportClick = onReportProductClick,
                onDismissRequest = onBottomSheetDismiss,
            )
        }
    }
}

@Composable
private fun ProductDetailDeleteDialog(
    enabled: Boolean,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
) {
    if (enabled) {
        NapzakDialog(
            title = stringResource(detail_dialog_delete_title),
            subTitle = stringResource(detail_dialog_delete_sub_title),
            dialogColor = NapzakDialogDefault.color.copy(
                titleColor = NapzakMarketTheme.colors.red
            ),
            onConfirmClick = onConfirmClick,
            onDismissClick = onDismissClick,
        )
    }
}

@Composable
private fun DeletedProductScreen(
    onHomeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(IntrinsicSize.Max),
        ) {
            Image(
                imageVector = ImageVector.vectorResource(img_not_found),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = stringResource(detail_deleted_product_title),
                style = NapzakMarketTheme.typography.title20sb,
                color = NapzakMarketTheme.colors.black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
            )

            Text(
                text = stringResource(detail_deleted_product_subtitle),
                style = NapzakMarketTheme.typography.body16m,
                color = NapzakMarketTheme.colors.gray300,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
            )

            NapzakButton(
                text = stringResource(detail_deleted_product_button),
                onClick = onHomeClick,
                icon = ImageVector.vectorResource(ic_thick_arrow),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp),
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 780, widthDp = 360)
@Composable
private fun ProductDetailScreenPreview() {
    val mockProductDetail = ProductDetail(
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
        shareUrl = "",
        productPhotos = listOf(
            ProductPhoto(
                photoId = 1,
                photoUrl = "",
                photoSequence = 1,
            )
        ),
        storeInfo = StoreInfo(
            userId = 1,
            storePhoto = "",
            nickname = "닉네임",
            totalSellCount = 1000,
            totalBuyCount = 1000,
        )
    )

    val tradeType = TradeType.fromName(mockProductDetail.tradeType)
    val tradeStatus = TradeStatusType.get(mockProductDetail.tradeStatus, tradeType)

    NapzakMarketTheme {
        SuccessScreen(
            productDetail = mockProductDetail,
            productPhotos = mockProductDetail.productPhotos.toImmutableList(),
            marketInfo = mockProductDetail.storeInfo,
            onMarketClick = {},
            modifier = Modifier,
            tradeType = tradeType,
            tradeStatus = tradeStatus,
        )
    }
}
