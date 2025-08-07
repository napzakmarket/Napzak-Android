package com.napzak.market.wishlist.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.component.tabbar.TradeTypeTabBar
import com.napzak.market.designsystem.component.topbar.NavigateUpTopBar
import com.napzak.market.feature.wishlist.R.string.wishlist_title
import com.napzak.market.ui_util.ShadowDirection
import com.napzak.market.ui_util.napzakGradientShadow

@Composable
internal fun WishlistTopSection(
    selectedTab: TradeType,
    onBackButtonClick: () -> Unit,
    onTabClick: (TradeType) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigateUpTopBar(
        title = stringResource(wishlist_title),
        onNavigateUp = onBackButtonClick,
        modifier = modifier,
    )

    TradeTypeTabBar(
        selectedTab = selectedTab,
        onTabClicked = onTabClick,
        modifier = Modifier
            .fillMaxWidth()
            .napzakGradientShadow(
                height = 2.dp,
                startColor = Color(0xFF000000), // TODO: shadow 컬러값 변경
                endColor = Color.Transparent,
                direction = ShadowDirection.Bottom,
            )
            .padding(horizontal = 20.dp),
    )
}
