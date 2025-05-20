package com.napzak.market.mypage.mypage.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.mypage.mypage.type.MyPageMenu
import com.napzak.market.util.android.noRippleClickable

private const val ROW_COUNT = 2
private const val COLUMN_COUNT = 3

@Composable
internal fun MyPageMenuCard(
    onSalesClick: () -> Unit,
    onPurchaseClick: () -> Unit,
    onRecentClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onHelpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val menus = remember {
        listOf(
                MyPageMenu.SALES to onSalesClick,
        MyPageMenu.PURCHASE to onPurchaseClick,
        MyPageMenu.RECENT to onRecentClick,
        MyPageMenu.FAVORITE to onFavoriteClick,
        MyPageMenu.SETTINGS to onSettingsClick,
        MyPageMenu.HELP to onHelpClick,
        )
    }

    val dividerColor = NapzakMarketTheme.colors.gray50

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(168.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NapzakMarketTheme.colors.gray10),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            for (rowIndex in 0 until ROW_COUNT) {
                Row(modifier = Modifier.weight(1f)) {
                    for (colIndex in 0 until COLUMN_COUNT) {
                        val index = rowIndex * COLUMN_COUNT + colIndex
                        val (menu, onClick) = menus[index]
                        val showRightBorder = colIndex < COLUMN_COUNT - 1
                        val showBottomBorder = rowIndex < ROW_COUNT - 1

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .noRippleClickable(onClick)
                                .drawBehind {
                                    val strokeWidth = 4.dp.toPx()
                                    if (showRightBorder) {
                                        drawLine(
                                            color = dividerColor,
                                            start = Offset(size.width, 0f),
                                            end = Offset(size.width, size.height),
                                            strokeWidth = strokeWidth,
                                        )
                                    }
                                    if (showBottomBorder) {
                                        drawLine(
                                            color = dividerColor,
                                            start = Offset(0f, size.height),
                                            end = Offset(size.width, size.height),
                                            strokeWidth = strokeWidth,
                                        )
                                    }
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(menu.iconRes),
                                    contentDescription = stringResource(menu.titleRes),
                                    tint = Color.Unspecified,
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                    text = stringResource(menu.titleRes),
                                    style = NapzakMarketTheme.typography.caption12sb,
                                    color = NapzakMarketTheme.colors.gray400,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyPageMenuCardPreview() {
    NapzakMarketTheme {
        MyPageMenuCard(
            onSalesClick = {},
            onPurchaseClick = {},
            onRecentClick = {},
            onFavoriteClick = {},
            onSettingsClick = {},
            onHelpClick = {},
        )
    }
}
