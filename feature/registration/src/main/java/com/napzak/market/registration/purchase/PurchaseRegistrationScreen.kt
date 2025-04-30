package com.napzak.market.registration.purchase

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.napzak.market.registration.component.PriceSettingGroup
import com.napzak.market.registration.component.RegistrationTopBar
import com.napzak.market.registration.component.RegistrationViewGroup
import com.napzak.market.registration.purchase.component.PriceNegotiationGroup
import com.napzak.market.registration.purchase.state.PurchaseContract.PurchaseSideEffect.NavigateToDetail
import com.napzak.market.registration.purchase.state.PurchaseContract.PurchaseUiState
import com.napzak.market.util.android.noRippleClickable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun PurchaseRegistrationRoute(
    navigateToUp: () -> Unit,
    navigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PurchaseRegistrationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
        uiState = uiState,
        onCloseClick = navigateToUp,
        onImageSelect = viewModel::updatePhotos,
        onPhotoPress = viewModel::updateRepresentPhoto,
        onDeleteClick = viewModel::deletePhoto,
        onGenreSelect = viewModel::updateGenre,
        onProductNameChange = viewModel::updateTitle,
        onProductDescriptionChange = viewModel::updateDescription,
        onPriceChange = viewModel::updatePrice,
        onNegotiableChange = viewModel::updateNegotiable,
        checkButtonEnabled = viewModel::updateButtonState,
        onRegisterClick = viewModel::registerProduct,
        modifier = modifier,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PurchaseRegistrationScreen(
    uiState: PurchaseUiState,
    onCloseClick: () -> Unit,
    onImageSelect: (ImmutableList<Uri>) -> Unit,
    onPhotoPress: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onGenreSelect: (String) -> Unit,
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
    val isButtonEnabled = remember(
        uiState.imageUris,
        uiState.genre,
        uiState.title,
        uiState.description,
        uiState.price,
        uiState.isNegotiable,
    ) { checkButtonEnabled() }

    LazyColumn(
        modifier = modifier
            .background(NapzakMarketTheme.colors.white)
            .noRippleClickable {
                focusManager.clearFocus()
            },
    ) {
        stickyHeader {
            RegistrationTopBar(
                title = stringResource(title, stringResource(purchase)),
                onCloseClick = onCloseClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        item {
            RegistrationViewGroup(
                productImageUris = uiState.imageUris.toPersistentList(),
                onImageSelect = onImageSelect,
                onPhotoPress = onPhotoPress,
                onDeleteClick = onDeleteClick,
                productGenre = uiState.genre,
                onGenreSelect = onGenreSelect,
                productName = uiState.title,
                onProductNameChange = onProductNameChange,
                productDescription = uiState.description,
                onProductDescriptionChange = onProductDescriptionChange,
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))

            PriceSettingGroup(
                tradeType = TradeType.BUY,
                title = stringResource(purchase_price),
                description = stringResource(purchase_price_description),
                price = uiState.price,
                onPriceChange = onPriceChange,
                priceTag = stringResource(purchase_price_tag),
                modifier = paddedModifier,
            )
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))

            PriceNegotiationGroup(
                isNegotiable = uiState.isNegotiable,
                onNegotiableChange = onNegotiableChange,
                modifier = paddedModifier.fillMaxWidth(),
            )
        }

        item {
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 4.dp,
                        spotColor = NapzakMarketTheme.colors.transBlack,
                        ambientColor = NapzakMarketTheme.colors.transBlack,
                    )
                    .background(NapzakMarketTheme.colors.white)
                    .then(paddedModifier)
                    .padding(top = 18.dp, bottom = 40.dp),
            ) {
                NapzakButton(
                    text = stringResource(register),
                    onClick = onRegisterClick,
                    enabled = isButtonEnabled,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PurchaseRegistrationScreenPreview() {
    NapzakMarketTheme {
        PurchaseRegistrationScreen(
            uiState = PurchaseUiState(),
            onCloseClick = {},
            onImageSelect = {},
            onPhotoPress = {},
            onDeleteClick = {},
            onGenreSelect = {},
            onProductNameChange = {},
            onProductDescriptionChange = {},
            onPriceChange = {},
            onNegotiableChange = {},
            checkButtonEnabled = { true },
            onRegisterClick = {},
        )
    }
}
