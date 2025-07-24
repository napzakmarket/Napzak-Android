package com.napzak.market.registration.purchase

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.component.button.NapzakButton
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.registration.R.string.purchase
import com.napzak.market.feature.registration.R.string.purchase_price
import com.napzak.market.feature.registration.R.string.purchase_price_description
import com.napzak.market.feature.registration.R.string.purchase_price_tag
import com.napzak.market.feature.registration.R.string.register
import com.napzak.market.feature.registration.R.string.title
import com.napzak.market.registration.RegistrationContract.RegistrationSideEffect.NavigateToDetail
import com.napzak.market.registration.RegistrationContract.RegistrationUiState
import com.napzak.market.registration.component.PriceSettingGroup
import com.napzak.market.registration.component.RegistrationTopBar
import com.napzak.market.registration.component.RegistrationViewGroup
import com.napzak.market.registration.model.Photo
import com.napzak.market.registration.purchase.component.PriceNegotiationGroup
import com.napzak.market.registration.purchase.state.PurchaseContract.PurchaseUiState
import com.napzak.market.ui_util.ShadowDirection
import com.napzak.market.ui_util.napzakGradientShadow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun PurchaseRegistrationRoute(
    navigateToUp: () -> Unit,
    navigateToDetail: (Long) -> Unit,
    navigateToGenreSearch: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PurchaseRegistrationViewModel = hiltViewModel(),
) {
    val registrationUiState by viewModel.registrationUiState.collectAsStateWithLifecycle()
    val purchaseUiState by viewModel.purchaseUiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycle = lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is NavigateToDetail -> navigateToDetail(sideEffect.productId)
                }
            }
    }

    PurchaseRegistrationScreen(
        registrationUiState = registrationUiState,
        purchaseUiState = purchaseUiState,
        onCloseClick = navigateToUp,
        onImageSelect = viewModel::updatePhotos,
        onPhotoPress = viewModel::updateRepresentPhoto,
        onDeleteClick = viewModel::deletePhoto,
        onGenreClick = { navigateToGenreSearch(registrationUiState.genre?.genreId) },
        onProductNameChange = viewModel::updateTitle,
        onProductDescriptionChange = viewModel::updateDescription,
        onPriceChange = viewModel::updatePrice,
        onNegotiableChange = viewModel::updateNegotiable,
        checkButtonEnabled = viewModel::updateButtonState,
        onRegisterClick = viewModel::getPresignedUrl,
        modifier = modifier,
    )
}

@OptIn(ExperimentalFoundationApi::class)
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
    val focusManager = LocalFocusManager.current
    val state = rememberLazyListState()

    LaunchedEffect(state.isScrollInProgress) {
        if (state.isScrollInProgress) {
            focusManager.clearFocus()
        }
    }

    Box(
        modifier = modifier
            .background(NapzakMarketTheme.colors.white),
    ) {
        LazyColumn(
            state = state,
        ) {
            stickyHeader {
                RegistrationTopBar(
                    title = stringResource(title, stringResource(purchase)),
                    onCloseClick = onCloseClick,
                    modifier = Modifier.fillMaxWidth(),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .napzakGradientShadow(
                            height = 2.dp,
                            startColor = Color(0x0D000000),
                            endColor = Color.Transparent,
                            direction = ShadowDirection.Bottom,
                        ),
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
                    modifier = paddedModifier,
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
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .napzakGradientShadow(
                    height = 2.dp,
                    startColor = Color(0x0D000000),
                    endColor = Color.Transparent,
                    direction = ShadowDirection.Top,
                )
                .background(NapzakMarketTheme.colors.white)
                .then(paddedModifier)
                .padding(top = 18.dp),
        ) {
            NapzakButton(
                text = stringResource(register),
                onClick = onRegisterClick,
                enabled = checkButtonEnabled(),
            )
        }
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
