package com.napzak.market.registration.purchase

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.R.drawable.ic_check_snackbar_18
import com.napzak.market.designsystem.component.loading.NapzakLoadingOverlay
import com.napzak.market.designsystem.component.toast.LocalNapzakToast
import com.napzak.market.designsystem.component.toast.ToastFontType
import com.napzak.market.designsystem.component.toast.ToastType
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.registration.R.string.purchase
import com.napzak.market.feature.registration.R.string.purchase_price
import com.napzak.market.feature.registration.R.string.purchase_price_description
import com.napzak.market.feature.registration.R.string.purchase_price_tag
import com.napzak.market.feature.registration.R.string.title
import com.napzak.market.registration.RegistrationContract.RegistrationSideEffect.NavigateToDetail
import com.napzak.market.registration.RegistrationContract.RegistrationSideEffect.ShowToast
import com.napzak.market.registration.RegistrationContract.RegistrationUiState
import com.napzak.market.registration.component.PriceSettingGroup
import com.napzak.market.registration.component.RegistrationButton
import com.napzak.market.registration.component.RegistrationTopBar
import com.napzak.market.registration.component.RegistrationViewGroup
import com.napzak.market.registration.model.Photo
import com.napzak.market.registration.purchase.component.PriceNegotiationGroup
import com.napzak.market.registration.purchase.state.PurchaseContract.PurchaseUiState
import com.napzak.market.ui_util.bringContentIntoView
import com.napzak.market.ui_util.clearFocusOnScrollConnection
import com.napzak.market.ui_util.nonClickableStickyHeader
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun PurchaseRegistrationRoute(
    navigateToUp: () -> Unit,
    navigateToDetail: (Long) -> Unit,
    navigateToGenreSearch: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PurchaseRegistrationViewModel = hiltViewModel(),
) {
    val registrationUiState by viewModel.registrationUiState.collectAsStateWithLifecycle()
    val purchaseUiState by viewModel.purchaseUiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val toast = LocalNapzakToast.current

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycle = lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is NavigateToDetail -> navigateToDetail(sideEffect.productId)
                    is ShowToast -> toast.makeText(
                        toastType = ToastType.COMMON,
                        message = sideEffect.message,
                        icon = ic_check_snackbar_18,
                        fontType = ToastFontType.LARGE,
                    )
                }
            }
    }

    BackHandler(registrationUiState.loadState is UiState.Loading) { /* no back press allowed*/ }

    if (registrationUiState.loadState is UiState.Loading) NapzakLoadingOverlay()

    PurchaseRegistrationScreen(
        registrationUiState = registrationUiState,
        purchaseUiState = purchaseUiState,
        onCloseClick = navigateToUp,
        onImageSelect = viewModel::updatePhotos,
        onPhotoPress = viewModel::updateRepresentPhoto,
        onDeleteClick = viewModel::deletePhoto,
        onGenreClick = navigateToGenreSearch,
        onProductNameChange = viewModel::updateTitle,
        onProductDescriptionChange = viewModel::updateDescription,
        onPriceChange = viewModel::updatePrice,
        onNegotiableChange = viewModel::updateNegotiable,
        checkButtonEnabled = viewModel::updateButtonState,
        onRegisterClick = viewModel::getPresignedUrl,
        modifier = modifier,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PurchaseRegistrationScreen(
    registrationUiState: RegistrationUiState,
    purchaseUiState: PurchaseUiState,
    onCloseClick: () -> Unit,
    onImageSelect: (ImmutableList<Photo>) -> Unit,
    onPhotoPress: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onGenreClick: () -> Unit,
    onProductNameChange: (String) -> Unit,
    onProductDescriptionChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onNegotiableChange: (Boolean) -> Unit,
    checkButtonEnabled: () -> Boolean,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val paddedModifier = Modifier.padding(horizontal = 20.dp)
    val state = rememberLazyListState()

    val density = LocalDensity.current
    var buttonHeight by remember { with(density) { mutableStateOf(0.dp) } }
    val isImeVisible = WindowInsets.isImeVisible
    val focusManager = LocalFocusManager.current
    val nestedScrollConnection = remember { clearFocusOnScrollConnection(focusManager) }

    LaunchedEffect(isImeVisible) {
        if (!isImeVisible) {
            focusManager.clearFocus(force = true)
        }
    }

    Box(
        modifier = modifier
            .background(NapzakMarketTheme.colors.white)
            .nestedScroll(nestedScrollConnection)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                    },
                )
            },
    ) {
        LazyColumn(
            modifier = Modifier
                .imePadding(),
            state = state,
            contentPadding = PaddingValues(
                bottom = if (isImeVisible) 0.dp else buttonHeight,
            ),
        ) {
            nonClickableStickyHeader {
                RegistrationTopBar(
                    title = stringResource(title, stringResource(purchase)),
                    onCloseClick = onCloseClick,
                )
            }

            item {
                RegistrationViewGroup(
                    productImageUris = registrationUiState.imageUris.toPersistentList(),
                    onImageSelect = onImageSelect,
                    onPhotoPress = onPhotoPress,
                    onDeleteClick = onDeleteClick,
                    productGenre = registrationUiState.genre?.genreName.orEmpty(),
                    onGenreClick = onGenreClick,
                    productName = registrationUiState.title,
                    onProductNameChange = onProductNameChange,
                    productDescription = registrationUiState.description,
                    onProductDescriptionChange = onProductDescriptionChange,
                    modifier = paddedModifier,
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                PriceSettingGroup(
                    tradeType = TradeType.BUY,
                    title = stringResource(purchase_price),
                    description = stringResource(purchase_price_description),
                    price = registrationUiState.price,
                    onPriceChange = onPriceChange,
                    priceTag = stringResource(purchase_price_tag),
                    modifier = paddedModifier.bringContentIntoView(),
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))

                PriceNegotiationGroup(
                    isNegotiable = purchaseUiState.isNegotiable,
                    onNegotiableChange = onNegotiableChange,
                    modifier = paddedModifier.fillMaxWidth(),
                )
            }
        }

        RegistrationButton(
            onRegisterClick = onRegisterClick,
            checkButtonEnabled = checkButtonEnabled(),
            modifier = paddedModifier
                .align(Alignment.BottomCenter)
                .onGloballyPositioned {
                    buttonHeight = with(density) { it.size.height.toDp() }
                },
        )
    }
}

@Preview
@Composable
private fun PurchaseRegistrationScreenPreview() {
    NapzakMarketTheme {
        PurchaseRegistrationScreen(
            registrationUiState = RegistrationUiState(),
            purchaseUiState = PurchaseUiState(),
            onCloseClick = {},
            onImageSelect = {},
            onPhotoPress = {},
            onDeleteClick = {},
            onGenreClick = {},
            onProductNameChange = {},
            onProductDescriptionChange = {},
            onPriceChange = {},
            onNegotiableChange = {},
            checkButtonEnabled = { true },
            onRegisterClick = {},
        )
    }
}
