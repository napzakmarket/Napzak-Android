package com.napzak.market.mypage.wishlist.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.R.drawable.ic_left_chevron
import com.napzak.market.designsystem.component.tabbar.TradeTypeTabBar
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.designsystem.theme.shadowBlack
import com.napzak.market.feature.mypage.R.string.wishlist_back_button
import com.napzak.market.feature.mypage.R.string.wishlist_title
import com.napzak.market.ui_util.ShadowDirection
import com.napzak.market.ui_util.napzakGradientShadow
import com.napzak.market.ui_util.noRippleClickable

@Composable
internal fun WishlistTopSection(
    selectedTab: TradeType,
    onBackButtonClick: () -> Unit,
    onTabClick: (TradeType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .napzakGradientShadow(
                height = 2.dp,
                startColor = shadowBlack,
                endColor = NapzakMarketTheme.colors.transWhite,
                direction = ShadowDirection.Bottom,
            )
            .padding(start = 20.dp, bottom = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(ic_left_chevron),
            contentDescription = stringResource(wishlist_back_button),
            tint = NapzakMarketTheme.colors.black,
            modifier = Modifier.noRippleClickable(onBackButtonClick),
        )

        Spacer(Modifier.width(6.dp))

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
        modifier = Modifier
            .fillMaxWidth()
            .napzakGradientShadow(
                height = 2.dp,
                startColor = shadowBlack,
                endColor = NapzakMarketTheme.colors.transWhite,
                direction = ShadowDirection.Bottom,
            )
            .padding(horizontal = 20.dp),
    )
}
