package com.napzak.market.designsystem.component.tabbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.theme.NapzakMarketTheme

/**
 * 탐색, 검색 결과, 장르 상세페이지에서 사용되는 탭바
 *
 * @param selectedTab 선택된 탭
 * @param onTabClicked 탭 클릭 시 실행됨
 * @param modifier 수정자
 */

@Composable
fun TradeTypeTabBar(
    selectedTab: TradeType,
    onTabClicked: (TradeType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(top = 6.dp),
    ) {
        TradeType.entries.forEachIndexed { index, tradeType ->
            TabBarItem(
                tabName = tradeType.label,
                isTabClicked = selectedTab == tradeType,
                onTabClick = { onTabClicked(tradeType) },
                modifier = Modifier.weight(1f),
            )

            if (index < TradeType.entries.size - 1) {
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}

@Preview
@Composable
private fun TradeTypeTabBarPreview() {
    NapzakMarketTheme {
        TradeTypeTabBar(
            selectedTab = TradeType.SELL,
            onTabClicked = { },
        )
    }
}