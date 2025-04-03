package com.napzak.market.designsystem.component.tabbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.util.android.noRippleClickable

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
        modifier = modifier,
    ) {
        TradeType.entries.forEachIndexed { index, tradeType ->
            TradeTypeTabBarItem(
                tabName = tradeType.label,
                isTabClicked = selectedTab == tradeType,
                onTabClick = { onTabClicked(tradeType) },
                modifier = Modifier.weight(1f)
            )

            if (index < TradeType.entries.size - 1) {
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}

@Composable
private fun TradeTypeTabBarItem(
    tabName: String,
    isTabClicked: Boolean,
    onTabClick: () -> Unit,
    textStyle: TextStyle = if (isTabClicked) NapzakMarketTheme.typography.body14b else NapzakMarketTheme.typography.body14sb,
    textColor: Color = if (isTabClicked) NapzakMarketTheme.colors.purple500 else NapzakMarketTheme.colors.gray200,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClickable(onTabClick),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(15.dp))
            Text(
                text = tabName,
                style = textStyle,
                color = textColor,
            )
            Spacer(Modifier.height(14.dp))
        }

        if (isTabClicked) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(color = NapzakMarketTheme.colors.purple500),
            )
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