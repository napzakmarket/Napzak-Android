package com.napzak.market.mypage.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.napzak.market.mypage.MyPageScreen
import com.napzak.market.mypage.model.MyPageViewModel


@Composable
fun MyPageRoute(
    onMyMarketClick: () -> Unit,
    onSalesClick: () -> Unit,
    onPurchaseClick: () -> Unit,
    onRecentClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onHelpClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MyPageScreen(
        nickname = uiState.nickname,
        profileImageUrl = uiState.profileImageUrl,
        salesCount = uiState.salesCount,
        purchaseCount = uiState.purchaseCount,
        onMyMarketClick = onMyMarketClick,
        onSalesClick = onSalesClick,
        onPurchaseClick = onPurchaseClick,
        onRecentClick = onRecentClick,
        onFavoriteClick = onFavoriteClick,
        onSettingsClick = onSettingsClick,
        onHelpClick = onHelpClick,
        modifier = modifier,
    )
}