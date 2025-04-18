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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.napzak.market.registration.component.PriceSettingGroup
import com.napzak.market.registration.component.RegistrationTopBar
import com.napzak.market.registration.component.RegistrationViewGroup
import com.napzak.market.registration.sale.component.ProductConditionGridButton
import com.napzak.market.registration.sale.component.ShippingFeeSelector
import com.napzak.market.util.android.noRippleClickable

@Composable
fun SaleRegistrationRoute(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SaleRegistrationScreen(
        onCloseClick = {},
        productImageUrls = emptyList(),
        onPhotoClick = {},
        onPhotoPress = {},
        onDeleteClick = {},
        productGenre = "",
        onGenreSelect = {},
        productName = "",
        onProductNameChange = {},
        productDescription = "",
        onProductDescriptionChange = {},
        productCondition = null,
        onProductConditionSelect = {},
        price = "",
        onPriceChange = {},
        isShippingFeeIncluded = false,
        onShippingFeeIncludedChange = {},
        isShippingFeeExcluded = false,
        onShippingFeeExcludedChange = {},
        isNormalShippingChecked = false,
        onNormalShippingCheckChange = {},
        normalShippingFee = "",
        onNormalShippingFeeChange = {},
        isHalfShippingChecked = false,
        onHalfShippingCheckChange = {},
        halfShippingFee = "",
        onHalfShippingFeeChange = {},
        onRegisterClick = {},
        modifier = modifier,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SaleRegistrationScreen(
    onCloseClick: () -> Unit,
    productImageUrls: List<String>,
    onPhotoClick: () -> Unit,
    onPhotoPress: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    productGenre: String,
    onGenreSelect: () -> Unit,
    productName: String,
    onProductNameChange: (String) -> Unit,
    productDescription: String,
    onProductDescriptionChange: (String) -> Unit,
    productCondition: ProductConditionType?,
    onProductConditionSelect: (ProductConditionType) -> Unit,
    price: String,
    onPriceChange: (String) -> Unit,
    isShippingFeeIncluded: Boolean,
    onShippingFeeIncludedChange: (Boolean) -> Unit,
    isShippingFeeExcluded: Boolean,
    onShippingFeeExcludedChange: (Boolean) -> Unit,
    isNormalShippingChecked: Boolean,
    onNormalShippingCheckChange: (Boolean) -> Unit,
    normalShippingFee: String,
    onNormalShippingFeeChange: (String) -> Unit,
    isHalfShippingChecked: Boolean,
    onHalfShippingCheckChange: (Boolean) -> Unit,
    halfShippingFee: String,
    onHalfShippingFeeChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val paddedModifier = Modifier.padding(horizontal = 20.dp)
    val focusManager = LocalFocusManager.current

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
                productImageUrls = productImageUrls,
                onPhotoClick = onPhotoClick,
                onPhotoPress = onPhotoPress,
                onDeleteClick = onDeleteClick,
                productGenre = productGenre,
                onGenreSelect = onGenreSelect,
                productName = productName,
                onProductNameChange = onProductNameChange,
                productDescription = productDescription,
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
                selectedCondition = productCondition,
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
                price = price,
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
                isShippingFeeIncluded = isShippingFeeIncluded,
                onShippingFeeIncludedChange = onShippingFeeIncludedChange,
                isShippingFeeExcluded = isShippingFeeExcluded,
                onShippingFeeExcludedChange = onShippingFeeExcludedChange,
                isNormalShippingChecked = isNormalShippingChecked,
                onNormalShippingCheckChange = onNormalShippingCheckChange,
                normalShippingFee = normalShippingFee,
                onNormalShippingFeeChange = onNormalShippingFeeChange,
                isHalfShippingChecked = isHalfShippingChecked,
                onHalfShippingCheckChange = onHalfShippingCheckChange,
                halfShippingFee = halfShippingFee,
                onHalfShippingFeeChange = onHalfShippingFeeChange,
                modifier = paddedModifier,
            )
        }

        item {
            Spacer(modifier = Modifier.height(if (isShippingFeeIncluded) 60.dp else 20.dp))

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
                    enabled = false,
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
            onCloseClick = {},
            productImageUrls = emptyList(),
            onPhotoClick = {},
            onPhotoPress = {},
            onDeleteClick = {},
            productGenre = "",
            onGenreSelect = {},
            productName = "",
            onProductNameChange = {},
            productDescription = "",
            onProductDescriptionChange = {},
            productCondition = null,
            onProductConditionSelect = {},
            price = "",
            onPriceChange = {},
            isShippingFeeIncluded = false,
            onShippingFeeIncludedChange = {},
            isShippingFeeExcluded = false,
            onShippingFeeExcludedChange = {},
            isNormalShippingChecked = false,
            onNormalShippingCheckChange = {},
            normalShippingFee = "",
            onNormalShippingFeeChange = {},
            isHalfShippingChecked = false,
            onHalfShippingCheckChange = {},
            halfShippingFee = "",
            onHalfShippingFeeChange = {},
            onRegisterClick = {},
        )
    }
}
