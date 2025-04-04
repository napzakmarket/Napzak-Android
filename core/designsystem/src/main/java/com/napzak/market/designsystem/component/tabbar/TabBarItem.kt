package com.napzak.market.designsystem.component.tabbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.util.android.noRippleClickable

@Composable
fun TabBarItem(
    tabName: String,
    isTabClicked: Boolean,
    onTabClick: () -> Unit,
    textStyle: TextStyle = if (isTabClicked) NapzakMarketTheme.typography.body14b else NapzakMarketTheme.typography.body14sb,
    textColor: Color = if (isTabClicked) NapzakMarketTheme.colors.purple500 else NapzakMarketTheme.colors.gray200,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClickable(onTabClick),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(9.dp))
            Text(
                text = tabName,
                style = textStyle,
                color = textColor,
            )
            Spacer(Modifier.height(9.dp))
        }

        Spacer(Modifier.height(5.dp))

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
private fun TabBarItemPreview(modifier: Modifier = Modifier) {
    NapzakMarketTheme {
        Row {
            TabBarItem(
                tabName = "팔아요",
                isTabClicked = true,
                onTabClick = { },
                modifier = modifier.weight(1f),
            )
            TabBarItem(
                tabName = "구해요",
                isTabClicked = false,
                onTabClick = { },
                modifier = modifier.weight(1f),
            )
        }
    }
}