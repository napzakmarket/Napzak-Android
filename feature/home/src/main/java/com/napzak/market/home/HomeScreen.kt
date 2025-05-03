package com.napzak.market.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.napzak.market.banner.Banner
import com.napzak.market.common.state.UiState
import com.napzak.market.designsystem.component.textfield.SearchTextField
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
import com.napzak.market.product.model.Product
import com.napzak.market.type.HomeBannerType
import com.napzak.market.util.android.ScreenPreview
import com.napzak.market.util.android.noRippleClickable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

@Composable
internal fun HomeRoute(
    onSearchNavigate: () -> Unit,
    onProductDetailNavigate: (Long) -> Unit,
    onMostInterestedSellNavigate: () -> Unit,
    onMostInterestedBuyNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by homeViewModel.homeUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        with(homeViewModel) {
            getBanners()
            getRecommendedProducts()
            getPopularSellProducts()
            getPopularBuyProducts()
        }
    }

    HomeScreen(
        uiState = uiState,
        onSearchTextFieldClick = onSearchNavigate,
        onProductClick = onProductDetailNavigate,
        onLikeButtonClick = { _, _ -> },
        onMostInterestedSellNavigate = onMostInterestedSellNavigate,
        onMostInterestedBuyNavigate = onMostInterestedBuyNavigate,
        modifier = modifier,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    onSearchTextFieldClick: () -> Unit,
    onProductClick: (Long) -> Unit,
    onLikeButtonClick: (Long, Boolean) -> Unit,
    onMostInterestedSellNavigate: () -> Unit,
    onMostInterestedBuyNavigate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        Column(
            modifier = modifier.background(NapzakMarketTheme.colors.white)
        ) {
            // TODO: 로고 탑바 추가

            when (uiState.isLoaded) {
                is UiState.Success -> HomeSuccessScreen(
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
                UiState.Loading -> {}
                UiState.Empty -> {}
            }
        }
    }
}

@Composable
private fun HomeSuccessScreen(
    productRecommends: ImmutableList<Product>,
    sellProducts: ImmutableList<Product>,
    buyProducts: ImmutableList<Product>,
    banners: ImmutableMap<HomeBannerType, List<Banner>>,
    onSearchTextFieldClick: () -> Unit,
    onProductClick: (Long) -> Unit,
    onLikeButtonClick: (Long, Boolean) -> Unit,
    onMostInterestedSellNavigate: () -> Unit,
    onMostInterestedBuyNavigate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(modifier = modifier.verticalScroll(scrollState)) {
        HomeSearchTextField(
            onClick = onSearchTextFieldClick,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 17.dp),
        )

        HorizontalAutoScrolledImages(
            images = listOf("", "", "").toImmutableList(), // TODO: 이미지 URL 리스트 대체
            onImageClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(360 / 216f)
                .padding(top = 20.dp),
        )

        HorizontalScrollableProducts(
            products = productRecommends, // TODO: Product 리스트 대체
            title = stringResource(home_list_customized_title),
            subTitle = stringResource(home_list_customized_sub_title),
            onProductClick = onProductClick,
            onLikeClick = onLikeButtonClick,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 32.dp),
        )

        HomeSingleBanner(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 50.dp),
        )

        VerticalGridProducts(
            products = sellProducts, // TODO: Product 리스트 대체
            title = stringResource(home_list_interested_sell_title),
            subTitle = stringResource(home_list_interested_sell_sub_title),
            onProductClick = onProductClick,
            onLikeClick = onLikeButtonClick,
            onMoreClick = onMostInterestedSellNavigate,
            modifier = Modifier
                .padding(top = 30.dp)
                .background(color = NapzakMarketTheme.colors.gray10)
                .padding(start = 20.dp, end = 20.dp, top = 32.dp, bottom = 20.dp),
        )

        VerticalGridProducts(
            products = buyProducts, // TODO: Product 리스트 대체
            title = stringResource(home_list_interested_buy_title),
            subTitle = stringResource(home_list_interested_buy_sub_title),
            onProductClick = onProductClick,
            onLikeClick = onLikeButtonClick,
            onMoreClick = onMostInterestedBuyNavigate,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 40.dp),
        )

        Spacer(modifier = Modifier.height(185.dp))
    }
}

@Composable
private fun HomeSearchTextField(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SearchTextField(
        text = "",
        hint = stringResource(home_search_text_field_hint),
        onTextChange = {},
        onSearchClick = {},
        onResetClick = {},
        enabled = false,
        readOnly = true,
        modifier = Modifier
            .noRippleClickable(onClick)
            .then(modifier),
    )
}

@Composable
private fun HomeSingleBanner(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = NapzakMarketTheme.colors.gray100),
    )
}

@ScreenPreview
@Composable
private fun HomeRoutePreview() {
    NapzakMarketTheme {
        HomeScreen(
            uiState = HomeUiState(
                bannerLoadState = UiState.Success(mapOf()),
                recommendProductLoadState = UiState.Success(listOf()),
                popularSellLoadState = UiState.Success(listOf()),
                popularBuyLoadState = UiState.Success(listOf()),
            ),
            onSearchTextFieldClick = {},
            onProductClick = {},
            onLikeButtonClick = { _, _ -> },
            onMostInterestedSellNavigate = { },
            onMostInterestedBuyNavigate = { },
        )
    }
}
