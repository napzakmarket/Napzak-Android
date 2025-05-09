package com.napzak.market.registration.sale

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
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
import com.napzak.market.common.type.ProductConditionType
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.component.button.NapzakButton
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.registration.R.string.product_condition
import com.napzak.market.feature.registration.R.string.register
import com.napzak.market.feature.registration.R.string.sale
import com.napzak.market.feature.registration.R.string.sale_price
import com.napzak.market.feature.registration.R.string.sale_price_description
import com.napzak.market.feature.registration.R.string.sale_price_tag
import com.napzak.market.feature.registration.R.string.shipping_method
import com.napzak.market.feature.registration.R.string.title
import com.napzak.market.registration.RegistrationContract.RegistrationSideEffect.NavigateToDetail
import com.napzak.market.registration.RegistrationContract.RegistrationUiState
import com.napzak.market.registration.component.PriceSettingGroup
import com.napzak.market.registration.component.RegistrationTopBar
import com.napzak.market.registration.component.RegistrationViewGroup
import com.napzak.market.registration.model.Photo
import com.napzak.market.registration.sale.component.ProductConditionGridButton
import com.napzak.market.registration.sale.component.ShippingFeeSelector
import com.napzak.market.registration.sale.state.SaleContract.SaleUiState
import com.napzak.market.util.android.noRippleClickable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun SaleRegistrationRoute(
    navigateToUp: () -> Unit,
    navigateToDetail: (Long) -> Unit,
    navigateToGenreSearch: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SaleRegistrationViewModel = hiltViewModel(),
) {
    val registrationUiState by viewModel.registrationUiState.collectAsStateWithLifecycle()
    val saleUiState by viewModel.saleUiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycle = lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is NavigateToDetail -> navigateToDetail(sideEffect.productId)
                }
            }
    }

    SaleRegistrationScreen(
        registrationUiState = registrationUiState,
        saleUiState = saleUiState,
        onCloseClick = navigateToUp,
        onImageSelect = viewModel::updatePhotos,
        onPhotoPress = viewModel::updateRepresentPhoto,
        onDeleteClick = viewModel::deletePhoto,
        onGenreClick = navigateToGenreSearch,
        onProductNameChange = viewModel::updateTitle,
        onProductDescriptionChange = viewModel::updateDescription,
        onProductConditionSelect = viewModel::updateCondition,
        onPriceChange = viewModel::updatePrice,
        onShippingFeeSelect = viewModel::updateShippingFeeInclusion,
        onNormalShippingFeeSelect = viewModel::updateNormalShippingFeeInclusion,
        onNormalShippingFeeChange = viewModel::updateNormalShippingFee,
        onHalfShippingFeeSelect = viewModel::updateHalfShippingFeeInclusion,
        onHalfShippingFeeChange = viewModel::updateHalfShippingFee,
        checkButtonEnabled = viewModel::updateButtonState,
        onRegisterClick = viewModel::registerProduct,
        modifier = modifier,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SaleRegistrationScreen(
    registrationUiState: RegistrationUiState,
    saleUiState: SaleUiState,
    onCloseClick: () -> Unit,
    onImageSelect: (ImmutableList<Photo>) -> Unit,
    onPhotoPress: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onGenreClick: () -> Unit,
    onProductNameChange: (String) -> Unit,
    onProductDescriptionChange: (String) -> Unit,
    onProductConditionSelect: (ProductConditionType) -> Unit,
    onPriceChange: (String) -> Unit,
    onShippingFeeSelect: (Boolean) -> Unit,
    onNormalShippingFeeSelect: (Boolean) -> Unit,
    onNormalShippingFeeChange: (String) -> Unit,
    onHalfShippingFeeSelect: (Boolean) -> Unit,
    onHalfShippingFeeChange: (String) -> Unit,
    checkButtonEnabled: () -> Boolean,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val paddedModifier = Modifier.padding(horizontal = 20.dp)
    val focusManager = LocalFocusManager.current
    val isButtonEnabled = remember(
        registrationUiState.imageUris,
        registrationUiState.genre,
        registrationUiState.title,
        registrationUiState.description,
        registrationUiState.price,
        saleUiState.isShippingFeeIncluded,
        saleUiState.isNormalShippingChecked,
        saleUiState.normalShippingFee,
        saleUiState.isHalfShippingChecked,
        saleUiState.halfShippingFee,
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
                title = stringResource(title, stringResource(sale)),
                onCloseClick = onCloseClick,
                modifier = Modifier
                    .fillMaxWidth(),
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

            Text(
                text = stringResource(product_condition),
                style = NapzakMarketTheme.typography.body14b.copy(
                    color = NapzakMarketTheme.colors.gray500,
                ),
                modifier = paddedModifier,
            )

            Spacer(modifier = Modifier.height(24.dp))

            ProductConditionGridButton(
                selectedCondition = saleUiState.condition,
                onConditionSelect = onProductConditionSelect,
                modifier = paddedModifier,
            )
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))

            PriceSettingGroup(
                tradeType = TradeType.SELL,
                title = stringResource(sale_price),
                description = stringResource(sale_price_description),
                price = registrationUiState.price,
                onPriceChange = onPriceChange,
                priceTag = stringResource(sale_price_tag),
                modifier = paddedModifier,
            )
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = stringResource(shipping_method),
                style = NapzakMarketTheme.typography.body14b.copy(
                    color = NapzakMarketTheme.colors.gray500,
                ),
                modifier = paddedModifier,
            )

            Spacer(modifier = Modifier.height(24.dp))

            ShippingFeeSelector(
                isShippingIncluded = saleUiState.isShippingFeeIncluded,
                onShippingFeeSelect = onShippingFeeSelect,
                isNormalShippingChecked = saleUiState.isNormalShippingChecked,
                onNormalShippingSelect = onNormalShippingFeeSelect,
                normalShippingFee = saleUiState.normalShippingFee,
                onNormalShippingFeeChange = onNormalShippingFeeChange,
                isHalfShippingChecked = saleUiState.isHalfShippingChecked,
                onHalfShippingSelect = onHalfShippingFeeSelect,
                halfShippingFee = saleUiState.halfShippingFee,
                onHalfShippingFeeChange = onHalfShippingFeeChange,
                modifier = paddedModifier,
            )
        }

        item {
            val spacerHeight = if (saleUiState.isShippingFeeIncluded == false) 60.dp else 20.dp

            Spacer(modifier = Modifier.height(spacerHeight))

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
private fun SaleRegistrationScreenPreview() {
    NapzakMarketTheme {
        SaleRegistrationScreen(
            registrationUiState = RegistrationUiState(),
            saleUiState = SaleUiState(),
            onCloseClick = {},
            onImageSelect = {},
            onPhotoPress = {},
            onDeleteClick = {},
            onGenreClick = {},
            onProductNameChange = {},
            onProductDescriptionChange = {},
            onProductConditionSelect = {},
            onPriceChange = {},
            onShippingFeeSelect = {},
            onNormalShippingFeeSelect = {},
            onNormalShippingFeeChange = {},
            onHalfShippingFeeSelect = {},
            onHalfShippingFeeChange = {},
            checkButtonEnabled = { true },
            onRegisterClick = {},
        )
    }
}
