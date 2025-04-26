package com.napzak.market.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.navigation.compose.NavHost
import com.napzak.market.detail.navigation.productDetailGraph
import com.napzak.market.dummy.navigation.dummyGraph
import com.napzak.market.home.navigation.homeGraph
import com.napzak.market.explore.navigation.exploreGraph
import com.napzak.market.explore.navigation.navigateToGenreDetail
import com.napzak.market.home.navigation.Home
import com.napzak.market.main.component.MainBottomBar
import com.napzak.market.main.component.MainRegisterDialog
import com.napzak.market.registration.navigation.navigateToPurchaseRegistration
import com.napzak.market.registration.navigation.navigateToSaleRegistration
import com.napzak.market.registration.navigation.registrationGraph
import com.napzak.market.search.navigation.navigateToSearch
import com.napzak.market.search.navigation.searchGraph
import com.napzak.market.store.store.navigation.storeGraph
import com.napzak.market.report.navigation.reportGraph
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
) {
    Scaffold(
        bottomBar = {
            MainBottomBar(
                isVisible = navigator.showBottomBar(),
                tabs = MainTab.entries.toImmutableList(),
                currentTab = navigator.currentTab,
                onTabSelected = navigator::navigate,
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding),
        ) {
            MainRegisterDialog(
                visibility = navigator.isRegister,
                onSellRegisterClick = navigator.navController::navigateToSaleRegistration,
                onBuyRegisterClick = navigator.navController::navigateToPurchaseRegistration,
                onDismissRequest = navigator::dismissRegisterDialog,
                modifier = Modifier.zIndex(1f),
            )

            MainNavHost(navigator = navigator)
        }
    }
}

@Composable
private fun MainNavHost(
    navigator: MainNavigator,
    modifier: Modifier = Modifier,
) {
    NavHost(
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        },
        popEnterTransition = {
            EnterTransition.None
        },
        popExitTransition = {
            ExitTransition.None
        },
        navController = navigator.navController,
        startDestination = navigator.startDestination
    ) {
        dummyGraph(modifier = modifier)

        homeGraph(
            navigateToSearch = { navigator.navController.navigateToSearch() },
            navigateToProductDetail = { /*TODO: 물품 상세페이지로 이동*/ },
            navigateToExploreSell = { /*TODO: 탐색>팔아요>인기상품순 */ },
            navigateToExploreBuy = { /*TODO: 탐색>구해요>인기상품순 */ },
            modifier = modifier,
        )

        exploreGraph(
            navigateToUp = { navigator.navController.popBackStack() },
            navigateToHome = { navigator.navController.popBackStack(Home, inclusive = false) },
            navigateToSearch = { navigator.navController.navigateToSearch() },
            navigateToGenreDetail = { genreId ->
                navigator.navController.navigateToGenreDetail(genreId)
            },
            navigateToProductDetail = { /* TODO: 상품 상세 화면으로 이동 */ },
            modifier = modifier,
        )

        searchGraph(
            navigateToPrevious = { navigator.navController.popBackStack() },
            navigateToSearchResult = { /* TODO: 검색어 검색결과 페이지로 이동 */ },
            navigateToGenreDetail = { genreId ->
                navigator.navController.navigateToGenreDetail(genreId)
            },
            modifier = modifier,
        )

        storeGraph(
            navigateToUp = navigator::navigateUp,
            navigateToProfileEdit = { /* TODO: move to profile edit */ },
            navigateToProductDetail = { /* TODO: move to product detail */ },
            modifier = modifier,
        )

        productDetailGraph(
            onMarketNavigate = {}, //TODO: 내마켓 화면으로 이동
            onChatNavigate = {}, //TODO: 채팅 화면으로 이동
            onModifyNavigate = {}, //TODO: 물품 정보 수정 화면으로 이동
            onReportNavigate = {}, //TODO: 물품 신고 화면으로 이동
            onNavigateUp = navigator::navigateUp,
            modifier = modifier
        )

        reportGraph(
            navigateUp = navigator::navigateUp,
            modifier = Modifier.systemBarsPadding()
        )

        registrationGraph(
            navigateToUp = navigator::navigateUp,
            navigateToDetail = {}, // TODO: 물품 상세 화면으로 이동
            modifier = modifier,
        )
    }
}
