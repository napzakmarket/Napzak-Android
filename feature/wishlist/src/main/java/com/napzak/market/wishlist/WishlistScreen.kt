package com.napzak.market.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.TradeStatusType
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.R.drawable.ic_left_chevron
import com.napzak.market.designsystem.R.drawable.img_empty_product
import com.napzak.market.designsystem.R.string.heart_click_snackbar_message
import com.napzak.market.designsystem.component.productItem.NapzakLargeProductItem
import com.napzak.market.designsystem.component.tabbar.TradeTypeTabBar
import com.napzak.market.designsystem.component.toast.LocalNapzakToast
import com.napzak.market.designsystem.component.toast.ToastType
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.wishlist.R.string.wishlist_back_button
import com.napzak.market.feature.wishlist.R.string.wishlist_title
import com.napzak.market.product.model.Product
import com.napzak.market.ui_util.noRippleClickable
import com.napzak.market.wishlist.state.WishlistUiState
import kotlin.collections.chunked

@Composable
internal fun WishlistRoute(
    modifier: Modifier = Modifier,
    viewModel: WishlistViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val toast = LocalNapzakToast.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.selectedTab) {
        viewModel.updateWishlistInformation()
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is WishlistSideEffect.ShowHeartToast -> {
                        toast.makeText(
                            toastType = ToastType.HEART,
                            message = context.getString(heart_click_snackbar_message),
                            yOffset = toast.toastOffsetWithBottomBar()
                        )
                    }

                    is WishlistSideEffect.CancelToast -> {
                        toast.cancel()
                    }
                }
            }
    }

    WishlistScreen(
        uiState = uiState,
        onBackButtonClick = {},
        onTabClick = viewModel::updateTradeType,
        onProductDetailNavigate = {},
        onLikeButtonClick = { id, value ->
            viewModel.updateProductIsInterested(productId = id, isInterested = value)
        },
        modifier = modifier,
    )
}

@Composable
private fun WishlistScreen(
    uiState: WishlistUiState,
    onBackButtonClick: () -> Unit,
    onTabClick: (TradeType) -> Unit,
    onProductDetailNavigate: (Long) -> Unit,
    onLikeButtonClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState.loadState) {
        is UiState.Loading -> {
        }

        is UiState.Empty -> {
            WishlistEmptyScreen(
                selectedTab = uiState.selectedTab,
                onBackButtonClick = onBackButtonClick,
                onTabClick = onTabClick,
                modifier = modifier,
            )
        }

        is UiState.Failure -> {
        }

        is UiState.Success -> {
            with(uiState) {
                WishlistSuccessScreen(
                    selectedTab = selectedTab,
                    products = uiState.loadState.data.interestProducts,
                    onBackButtonClick = onBackButtonClick,
                    onTabClick = onTabClick,
                    onProductDetailNavigate = onProductDetailNavigate,
                    onLikeButtonClick = onLikeButtonClick,
                    modifier = modifier,
                )
            }
        }
    }
}

@Composable
fun WishlistEmptyScreen(
    selectedTab: TradeType,
    onBackButtonClick: () -> Unit,
    onTabClick: (TradeType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NapzakMarketTheme.colors.white)
            .padding(top = 58.dp),
    ) {
        WishlistTopSection(
            selectedTab = selectedTab,
            onBackButtonClick = onBackButtonClick,
            onTabClick = onTabClick,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .align(alignment = Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(img_empty_product),
                contentDescription = null,
                tint = Color.Unspecified,
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "아직 찜한 소장품이 없어요",
                style = NapzakMarketTheme.typography.body16sb.copy(
                    color = NapzakMarketTheme.colors.gray300,
                )
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "아직 찜한 소장품이 없어요",
                style = NapzakMarketTheme.typography.caption12sb.copy(
                    color = NapzakMarketTheme.colors.gray200,
                )
            )
        }
    }
}

@Composable
private fun WishlistSuccessScreen(
    selectedTab: TradeType,
    products: List<Product>,
    onBackButtonClick: () -> Unit,
    onTabClick: (TradeType) -> Unit,
    onProductDetailNavigate: (Long) -> Unit,
    onLikeButtonClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NapzakMarketTheme.colors.white)
            .padding(top = 58.dp),
    ) {
        WishlistTopSection(
            selectedTab = selectedTab,
            onBackButtonClick = onBackButtonClick,
            onTabClick = onTabClick,
        )

        WishlistProducts(
            products = products,
            onProductClick = onProductDetailNavigate,
            onLikeButtonClick = onLikeButtonClick,
        )
    }
}

@Composable
private fun WishlistTopSection(
    selectedTab: TradeType,
    onBackButtonClick: () -> Unit,
    onTabClick: (TradeType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(start = 20.dp, bottom = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(ic_left_chevron),
            contentDescription = stringResource(wishlist_back_button),
            tint = NapzakMarketTheme.colors.black,
            modifier = Modifier.noRippleClickable(onBackButtonClick),
        )

        Spacer(Modifier.width(4.dp))

        Text(
            text = stringResource(wishlist_title),
            style = NapzakMarketTheme.typography.body16b.copy(
                color = NapzakMarketTheme.colors.black,
            )
        )
    }

    TradeTypeTabBar(
        selectedTab = selectedTab,
        onTabClicked = onTabClick,
        modifier = Modifier.padding(horizontal = 20.dp),
    )
}

@Composable
private fun WishlistProducts(
    products: List<Product>,
    onProductClick: (Long) -> Unit,
    onLikeButtonClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = NapzakMarketTheme.colors.white),
    ) {
        itemsIndexed(products.chunked(2)) { index, rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                rowItems.forEach { product ->
                    with(product) {
                        NapzakLargeProductItem(
                            genre = genreName,
                            title = productName,
                            imgUrl = photo,
                            price = price.toString(),
                            createdDate = uploadTime,
                            reviewCount = chatCount.toString(),
                            likeCount = interestCount.toString(),
                            isLiked = isInterested,
                            isMyItem = isOwnedByCurrentUser,
                            isSellElseBuy = TradeType.valueOf(tradeType) == TradeType.SELL,
                            isSuggestionAllowed = isPriceNegotiable,
                            tradeStatus = TradeStatusType.get(
                                tradeStatus, TradeType.valueOf(tradeType)
                            ),
                            onLikeClick = { onLikeButtonClick(productId, isInterested) },
                            modifier = Modifier
                                .weight(1f)
                                .noRippleClickable { onProductClick(productId) },
                        )
                    }
                }

                if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }

        item {
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Preview
@Composable
private fun WishlistSuccessScreenPreview(modifier: Modifier = Modifier) {
    NapzakMarketTheme {
//        WishlistSuccessScreen(
//            selectedTab = TradeType.SELL,
//            products = emptyList(),
//            onBackButtonClick = {},
//            onTabClick = {},
//            onProductDetailNavigate = {},
//            onLikeButtonClick = { id, value -> },
//            modifier = modifier,
//        )

        WishlistEmptyScreen(
            selectedTab = TradeType.SELL,
            onBackButtonClick = {},
            onTabClick = {},
        )
    }
}
