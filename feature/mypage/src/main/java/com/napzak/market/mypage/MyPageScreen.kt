package com.napzak.market.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.designsystem.R.drawable.ic_arrow_right_7
import com.napzak.market.designsystem.R.drawable.ic_help_32
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.mypage.R.string.buy_count
import com.napzak.market.feature.mypage.R.string.buy_label
import com.napzak.market.feature.mypage.R.string.my_market
import com.napzak.market.feature.mypage.R.string.profile_image_description
import com.napzak.market.feature.mypage.R.string.sell_count
import com.napzak.market.feature.mypage.R.string.sell_label
import com.napzak.market.util.android.noRippleClickable

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    nickname: String,
    salesCount: Int,
    purchaseCount: Int,
    onMyMarketClick: () -> Unit,
    onSalesClick: () -> Unit,
    onPurchaseClick: () -> Unit,
    onRecentClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onHelpClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NapzakMarketTheme.colors.gray10),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(NapzakMarketTheme.colors.white)
                .padding(horizontal = 20.dp),
        ) {
            Text(
                text = "Napzak(Logo)", //임시 로고라 stringX
                color = Color.White,
                modifier = Modifier
                    .padding(top = 60.dp)
                    .background(NapzakMarketTheme.colors.purple500)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(94.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(NapzakMarketTheme.colors.gray10)
                    .padding(horizontal = 22.dp, vertical = 17.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            color = NapzakMarketTheme.colors.purple100,
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .placeholder(ic_help_32)
                            .error(ic_help_32)
                            .build(),
                        contentDescription = stringResource(profile_image_description),
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = nickname,
                        color = NapzakMarketTheme.colors.purple500,
                        style = NapzakMarketTheme.typography.body14b,
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    Row {
                        Text(
                            text = stringResource(sell_label),
                            color = NapzakMarketTheme.colors.gray500,
                            style = NapzakMarketTheme.typography.caption12m,
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = stringResource(sell_count, salesCount),
                            color = NapzakMarketTheme.colors.gray500,
                            style = NapzakMarketTheme.typography.caption12sb,
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = stringResource(buy_label),
                            color = NapzakMarketTheme.colors.gray500,
                            style = NapzakMarketTheme.typography.caption12m,
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = stringResource(buy_count, purchaseCount),
                            color = NapzakMarketTheme.colors.gray500,
                            style = NapzakMarketTheme.typography.caption12sb,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(NapzakMarketTheme.colors.gray10)
                    .padding(vertical = 13.dp)
                    .noRippleClickable(onMyMarketClick),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(my_market),
                    style = NapzakMarketTheme.typography.caption12sb,
                    color = NapzakMarketTheme.colors.gray300,
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = ImageVector.vectorResource(ic_arrow_right_7),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            HorizontalDivider(
                color = NapzakMarketTheme.colors.gray10,
                thickness = 4.dp,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(NapzakMarketTheme.colors.white)
                .padding(horizontal = 20.dp),
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(168.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NapzakMarketTheme.colors.gray10),
            ) {
                val menus = listOf(
                    MyPageMenu.SALES to onSalesClick,
                    MyPageMenu.PURCHASE to onPurchaseClick,
                    MyPageMenu.RECENT to onRecentClick,
                    MyPageMenu.FAVORITE to onFavoriteClick,
                    MyPageMenu.SETTINGS to onSettingsClick,
                    MyPageMenu.HELP to onHelpClick,
                )

                val dividerColor = NapzakMarketTheme.colors.gray50

                Column(modifier = Modifier.fillMaxSize()) {
                    for (rowIndex in 0 until 2) {
                        Row(modifier = Modifier.weight(1f)) {
                            for (colIndex in 0 until 3) {
                                val index = rowIndex * 3 + colIndex
                                val (menu, onClick) = menus[index]

                                val showRightBorder = colIndex < 2
                                val showBottomBorder = rowIndex < 1

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
                                            imageVector = ImageVector.vectorResource(id = menu.iconRes),
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

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
fun MyPageScreenPreview() {
    NapzakMarketTheme {
        MyPageScreen(
            modifier = Modifier,
            nickname = "납작한자기",
            salesCount = 31,
            purchaseCount = 15,
            onMyMarketClick = { println("내 마켓 보기 클릭") },
            onSalesClick = { println("판매 내역 클릭") },
            onPurchaseClick = { println("구매 내역 클릭") },
            onRecentClick = { println("최근 본 상품 클릭") },
            onFavoriteClick = { println("찜 클릭") },
            onSettingsClick = { println("설정 클릭") },
            onHelpClick = { println("고객센터 클릭") },
        )
    }
}