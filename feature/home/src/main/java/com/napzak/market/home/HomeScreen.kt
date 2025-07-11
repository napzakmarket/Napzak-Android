package com.napzak.market.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.banner.Banner
import com.napzak.market.common.state.UiState
import com.napzak.market.designsystem.R.string.heart_click_snackbar_message
import com.napzak.market.designsystem.component.NapzakLoadingOverlay
import com.napzak.market.designsystem.component.textfield.SearchTextField
import com.napzak.market.designsystem.component.toast.LocalNapzakToast
import com.napzak.market.designsystem.component.toast.ToastType
import com.napzak.market.designsystem.component.topbar.NapzakLogoTopBar
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.home.R.string.home_list_customized_sub_title
import com.napzak.market.feature.home.R.string.home_list_customized_title
import com.napzak.market.feature.home.R.string.home_list_interested_buy_sub_title
import com.napzak.market.feature.home.R.string.home_list_interested_buy_title
import com.napzak.market.feature.home.R.string.home_list_interested_sell_sub_title
import com.napzak.market.feature.home.R.string.home_list_interested_sell_title
import com.napzak.market.feature.home.R.string.home_search_text_field_hint
import com.napzak.market.home.component.HorizontalAutoScrolledImages
import com.napzak.market.home.component.HorizontalScrollableProducts
import com.napzak.market.home.component.VerticalGridProducts
import com.napzak.market.home.state.HomeUiState
import com.napzak.market.home.type.HomeProductType
import com.napzak.market.product.model.Product
import com.napzak.market.type.HomeBannerType
import com.napzak.market.ui_util.ScreenPreview
import com.napzak.market.ui_util.ellipsis
import com.napzak.market.ui_util.noRippleClickable
import com.napzak.market.ui_util.openUrl
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

private const val NICKNAME_MAX_LENGTH = 10

@Composable
internal fun HomeRoute(
    onSearchNavigate: () -> Unit,
    onProductDetailNavigate: (Long) -> Unit,
    onMostInterestedSellNavigate: () -> Unit,
    onMostInterestedBuyNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val napzakToast = LocalNapzakToast.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchHomeData()
    }

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle).collect {
            when (it) {
                HomeSideEffect.ShowInterestToast -> {
                    napzakToast.makeText(
                        toastType = ToastType.HEART,
                        message = context.getString(heart_click_snackbar_message),
                    )
                }

                HomeSideEffect.CancelInterestToast -> {
                    napzakToast.cancel()
                }
            }
        }
    }

    HomeScreen(
        uiState = uiState,
        onSearchTextFieldClick = onSearchNavigate,
        onProductClick = onProductDetailNavigate,
        onLikeButtonClick = viewModel::setInterest,
        onMostInterestedSellNavigate = onMostInterestedSellNavigate,
        onMostInterestedBuyNavigate = onMostInterestedBuyNavigate,
        modifier = modifier,
    )
}

@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    onSearchTextFieldClick: () -> Unit,
    onProductClick: (Long) -> Unit,
    onLikeButtonClick: (Long, Boolean, HomeProductType) -> Unit,
    onMostInterestedSellNavigate: () -> Unit,
    onMostInterestedBuyNavigate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.background(NapzakMarketTheme.colors.white),
    ) {
        NapzakLogoTopBar(modifier = Modifier.padding(horizontal = 20.dp, vertical = 17.dp))

        when (uiState.isLoaded) {
            is UiState.Success -> HomeSuccessScreen(
                nickname = uiState.nickname.ellipsis(NICKNAME_MAX_LENGTH),
                productRecommends = (uiState.recommendProductLoadState as UiState.Success<List<Product>>).data.toImmutableList(),
                sellProducts = (uiState.popularSellLoadState as UiState.Success<List<Product>>).data.toImmutableList(),
                buyProducts = (uiState.popularBuyLoadState as UiState.Success<List<Product>>).data.toImmutableList(),
                banners = (uiState.bannerLoadState as UiState.Success<Map<HomeBannerType, List<Banner>>>).data.toImmutableMap(),
                onSearchTextFieldClick = onSearchTextFieldClick,
                onProductClick = onProductClick,
                onLikeButtonClick = onLikeButtonClick,
                onMostInterestedSellNavigate = onMostInterestedSellNavigate,
                onMostInterestedBuyNavigate = onMostInterestedBuyNavigate,
            )

            is UiState.Failure -> {}
            is UiState.Loading -> NapzakLoadingOverlay()
            is UiState.Empty -> {}
        }
    }
}

@Composable
private fun HomeSuccessScreen(
    nickname: String,
    productRecommends: ImmutableList<Product>,
    sellProducts: ImmutableList<Product>,
    buyProducts: ImmutableList<Product>,
    banners: ImmutableMap<HomeBannerType, List<Banner>>,
    onSearchTextFieldClick: () -> Unit,
    onProductClick: (Long) -> Unit,
    onLikeButtonClick: (Long, Boolean, HomeProductType) -> Unit,
    onMostInterestedSellNavigate: () -> Unit,
    onMostInterestedBuyNavigate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    LazyColumn(modifier = modifier) {
        item {
            SearchTextField(
                text = "",
                hint = stringResource(home_search_text_field_hint),
                onTextChange = {},
                onSearchClick = {},
                onResetClick = {},
                enabled = false,
                readOnly = true,
                modifier = Modifier
                    .noRippleClickable(onSearchTextFieldClick)
                    .padding(horizontal = 20.dp),
            )
        }

        item {
            banners[HomeBannerType.TOP]?.let { topBanners ->
                HorizontalAutoScrolledImages(
                    images = topBanners.map { it.imageUrl }.toImmutableList(),
                    onImageClick = { index ->
                        runCatching {
                            context.openUrl(topBanners[index].linkUrl)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(360 / 216f)
                        .padding(top = 20.dp),
                )
            }
        }

        item {
            HorizontalScrollableProducts(
                products = productRecommends,
                title = stringResource(home_list_customized_title, nickname),
                subTitle = stringResource(home_list_customized_sub_title, nickname),
                onProductClick = onProductClick,
                onLikeClick = { productId, isInterest ->
                    onLikeButtonClick(productId, isInterest, HomeProductType.RECOMMEND)
                },
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 32.dp),
            )
        }

        item {
            banners[HomeBannerType.MIDDLE]?.let { banner ->
                HomeSingleBanner(
                    banner = banner.first(),
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 40.dp),
                )
            }
        }

        item {
            VerticalGridProducts(
                products = sellProducts,
                title = stringResource(home_list_interested_sell_title),
                subTitle = stringResource(home_list_interested_sell_sub_title),
                onProductClick = onProductClick,
                onLikeClick = { productId, isInterest ->
                    onLikeButtonClick(productId, isInterest, HomeProductType.POPULAR_SELL)
                },
                onMoreClick = onMostInterestedSellNavigate,
                modifier = Modifier
                    .padding(top = 30.dp)
                    .background(color = NapzakMarketTheme.colors.gray10)
                    .padding(start = 20.dp, end = 20.dp, top = 32.dp, bottom = 20.dp),
            )
        }

        item {
            banners[HomeBannerType.BOTTOM]?.let { banner ->
                HomeSingleBanner(
                    banner = banner.first(),
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 40.dp),
                )
            }
        }

        item {
            VerticalGridProducts(
                products = buyProducts,
                title = stringResource(home_list_interested_buy_title),
                subTitle = stringResource(home_list_interested_buy_sub_title),
                onProductClick = onProductClick,
                onLikeClick = { productId, isInterest ->
                    onLikeButtonClick(productId, isInterest, HomeProductType.POPULAR_BUY)
                },
                onMoreClick = onMostInterestedBuyNavigate,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 40.dp),
            )
        }


        item {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(165.dp)
                    .background(NapzakMarketTheme.colors.gray10)
            )
        }
    }
}

@Composable
private fun HomeSingleBanner(
    banner: Banner,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    AsyncImage(
        model = ImageRequest
            .Builder(context)
            .data(banner.imageUrl)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .noRippleClickable { context.openUrl(banner.linkUrl) }
            .fillMaxWidth()
            .height(110.dp)
            .clip(RoundedCornerShape(16.dp)),
    )
}

@ScreenPreview
@Composable
private fun HomeRoutePreview() {
    val mockProducts = buildList {
        repeat(5) { index ->
            add(
                Product(
                    productId = index.toLong(),
                    genreName = "장르",
                    productName = "물품 이름",
                    photo = "",
                    price = 10000,
                    uploadTime = "등록일",
                    isInterested = false,
                    tradeType = "SELL",
                    tradeStatus = "BEFORE_TRADE",
                    isPriceNegotiable = false,
                    isOwnedByCurrentUser = false,
                    interestCount = 5,
                    chatCount = 5
                )
            )
        }
    }

    NapzakMarketTheme {
        HomeScreen(
            uiState = HomeUiState(
                nickname = "납자기장독대나무다리어카센터미널뛰기러기찻길동무",
                bannerLoadState = UiState.Success(mapOf()),
                recommendProductLoadState = UiState.Success(mockProducts),
                popularSellLoadState = UiState.Success(mockProducts),
                popularBuyLoadState = UiState.Success(mockProducts),
            ),
            onSearchTextFieldClick = {},
            onProductClick = {},
            onLikeButtonClick = { _, _, _ -> },
            onMostInterestedSellNavigate = { },
            onMostInterestedBuyNavigate = { },
        )
    }
}
