package com.napzak.market.registration.purchase

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.napzak.market.util.android.noRippleClickable

@Composable
fun PurchaseRegistrationRoute(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PurchaseRegistrationScreen(
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
        price = "",
        onPriceChange = {},
        isNegotiable = false,
        onNegotiableChange = {},
        onRegisterClick = {},
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PurchaseRegistrationScreen(
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
    price: String,
    onPriceChange: (String) -> Unit,
    isNegotiable: Boolean,
    onNegotiableChange: (Boolean) -> Unit,
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
                title = stringResource(title, stringResource(purchase)),
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

            PriceSettingGroup(
                tradeType = TradeType.BUY,
                title = stringResource(purchase_price),
                description = stringResource(purchase_price_description),
                price = price,
                onPriceChange = onPriceChange,
                priceTag = stringResource(purchase_price_tag),
                modifier = paddedModifier,
            )
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))

            PriceNegotiationGroup(
                isNegotiable = isNegotiable,
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
                    enabled = false,
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
            price = "",
            onPriceChange = {},
            onCloseClick = {},
            isNegotiable = true,
            onNegotiableChange = {},
            onRegisterClick = {},
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
        )
    }
}
