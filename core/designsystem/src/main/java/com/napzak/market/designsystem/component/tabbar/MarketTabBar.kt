package com.napzak.market.designsystem.component.tabbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.common.type.MarketTab
import com.napzak.market.designsystem.theme.NapzakMarketTheme

/**
 * 마켓페이지에서 사용되는 탭바
 *
 * @param selectedTab 선택된 탭
 * @param onTabClicked 탭 클릭 시 실행됨
 * @param modifier 수정자
 */

@Composable
fun MarketTabBar(
    selectedTab: MarketTab,
    onTabClicked: (MarketTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(top = 6.dp, start = 16.dp, end = 16.dp),
    ) {
        MarketTab.entries.forEachIndexed { index, marketTab ->
            TabBarItem(
                tabName = marketTab.label,
                isTabClicked = selectedTab == marketTab,
                onTabClick = { onTabClicked(marketTab) },
                modifier = Modifier.weight(1f),
            )

            if (index < MarketTab.entries.size - 1) {
                Spacer(modifier = Modifier.width(12.dp))
            }
        }
    }
}

@Preview
@Composable
private fun MarketTabBarPreview(modifier: Modifier = Modifier) {
    NapzakMarketTheme {
        MarketTabBar(
            selectedTab = MarketTab.SELL,
            onTabClicked = { },
            modifier = modifier,
        )
    }
}