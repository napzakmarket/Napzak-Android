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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import com.napzak.market.home.model.Product
import com.napzak.market.util.android.ScreenPreview
import com.napzak.market.util.android.noRippleClickable
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun HomeRoute(
    onSearchNavigate: () -> Unit,
    onProductDetailNavigate: (Long) -> Unit,
    onMostInterestedSellNavigate: () -> Unit,
    onMostInterestedBuyNavigate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeScreen(
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
    onSearchTextFieldClick: () -> Unit,
    onProductClick: (Long) -> Unit,
    onLikeButtonClick: (Long, Boolean) -> Unit,
    onMostInterestedSellNavigate: () -> Unit,
    onMostInterestedBuyNavigate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        Column(
            modifier = modifier.background(NapzakMarketTheme.colors.white)
        ) {
            // TODO: 로고 탑바 추가

            Column(modifier = Modifier.verticalScroll(scrollState)) {
                HomeSearchTextField(
                    onClick = onSearchTextFieldClick,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 17.dp),
                )

                HorizontalAutoScrolledImages(
                    images = listOf("", "", "").toImmutableList(),
                    onImageClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(360 / 216f)
                        .padding(top = 22.dp),
                )

                HomeSingleBanner(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp),
                )

                HorizontalScrollableProducts(
                    products = Product.mockMixedProduct.toImmutableList(),
                    title = stringResource(home_list_customized_title),
                    subTitle = stringResource(home_list_customized_sub_title),
                    onProductClick = onProductClick,
                    onLikeClick = onLikeButtonClick,
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .background(NapzakMarketTheme.colors.gray10)
                        .padding(start = 20.dp, end = 20.dp, top = 32.dp, bottom = 20.dp),
                )

                VerticalGridProducts(
                    products = Product.mockMixedProduct,
                    title = stringResource(home_list_interested_sell_title),
                    subTitle = stringResource(home_list_interested_sell_sub_title),
                    onProductClick = onProductClick,
                    onLikeClick = onLikeButtonClick,
                    onMoreClick = onMostInterestedSellNavigate,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 40.dp),
                )

                VerticalGridProducts(
                    products = Product.mockMixedProduct,
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
            onSearchTextFieldClick = {},
            onProductClick = {},
            onLikeButtonClick = { _, _ -> },
            onMostInterestedSellNavigate = { },
            onMostInterestedBuyNavigate = { },
        )
    }
}
