package com.napzak.market.mypage.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.napzak.market.designsystem.component.topbar.NapzakLogoTopBar
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.mypage.mypage.component.MyMarketButton
import com.napzak.market.mypage.mypage.component.MyPageMenuCard
import com.napzak.market.mypage.mypage.component.MyPageProfileSection
import com.napzak.market.mypage.mypage.state.MyPageUiState
import com.napzak.market.ui_util.ScreenPreview
import com.napzak.market.util.common.openUrl


@Composable
internal fun MyPageRoute(
    onMyMarketClick: (Long) -> Unit,
    onSalesClick: () -> Unit,
    onPurchaseClick: () -> Unit,
    onRecentClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchStoreInfo()
    }

    MyPageScreen(
        uiState = uiState,
        onMyMarketClick = { onMyMarketClick(uiState.storeId) },
        onSalesClick = onSalesClick,
        onPurchaseClick = onPurchaseClick,
        onRecentClick = onRecentClick,
        onFavoriteClick = onFavoriteClick,
        onSettingsClick = onSettingsClick,
        onHelpClick = {
            if (uiState.serviceLink.isNotBlank()) {
                context.openUrl(uiState.serviceLink)
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun MyPageScreen(
    uiState: MyPageUiState,
    onMyMarketClick: () -> Unit,
    onSalesClick: () -> Unit,
    onPurchaseClick: () -> Unit,
    onRecentClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onHelpClick: () -> Unit,
    modifier: Modifier = Modifier,
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
            NapzakLogoTopBar(modifier = Modifier.padding(vertical = 17.dp))
            Spacer(modifier = Modifier.height(30.dp))
            MyPageProfileSection(
                nickname = uiState.nickname,
                profileImageUrl = uiState.profileImageUrl,
                salesCount = uiState.salesCount,
                purchaseCount = uiState.purchaseCount,
            )
            Spacer(modifier = Modifier.height(20.dp))
            MyMarketButton(
                onClick = onMyMarketClick,
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        HorizontalDivider(
            color = NapzakMarketTheme.colors.gray10,
            thickness = 4.dp,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(NapzakMarketTheme.colors.white)
                .padding(horizontal = 20.dp),
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            MyPageMenuCard(
                onSalesClick = onSalesClick,
                onPurchaseClick = onPurchaseClick,
                onRecentClick = onRecentClick,
                onFavoriteClick = onFavoriteClick,
                onSettingsClick = onSettingsClick,
                onHelpClick = onHelpClick,
            )
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@ScreenPreview
@Composable
private fun MyPageScreenPreview() {
    NapzakMarketTheme {
        MyPageScreen(
            uiState = MyPageUiState(
                nickname = "납작한자기",
                profileImageUrl = "https://via.placeholder.com/150",
                salesCount = 31,
                purchaseCount = 15,
            ),
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