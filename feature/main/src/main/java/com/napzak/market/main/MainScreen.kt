package com.napzak.market.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.compose.NavHost
import com.napzak.market.designsystem.component.CommonSnackBar
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.detail.navigation.navigateToProductDetail
import com.napzak.market.detail.navigation.productDetailGraph
import com.napzak.market.dummy.navigation.dummyGraph
import com.napzak.market.explore.navigation.exploreGraph
import com.napzak.market.explore.navigation.navigateToGenreDetail
import com.napzak.market.home.navigation.Home
import com.napzak.market.home.navigation.homeGraph
import com.napzak.market.main.component.MainBottomBar
import com.napzak.market.main.component.MainRegisterDialog
import com.napzak.market.mypage.navigation.mypageGraph
import com.napzak.market.onboarding.navigation.Terms
import com.napzak.market.onboarding.navigation.onboardingGraph
import com.napzak.market.registration.navigation.navigateToGenreSearch
import com.napzak.market.registration.navigation.navigateToPurchaseRegistration
import com.napzak.market.registration.navigation.navigateToSaleRegistration
import com.napzak.market.registration.navigation.registrationGraph
import com.napzak.market.report.navigation.navigateToProductReport
import com.napzak.market.report.navigation.reportGraph
import com.napzak.market.search.navigation.navigateToSearch
import com.napzak.market.search.navigation.searchGraph
import com.napzak.market.store.store.navigation.navigateToStore
import com.napzak.market.store.store.navigation.storeGraph
import com.napzak.market.util.android.LocalSnackBarController
import com.napzak.market.util.android.SnackBarController
import kotlinx.collections.immutable.toImmutableList


@Composable
fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
) {
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val snackBarController = remember { SnackBarController(snackBarHostState, coroutineScope) }

    Scaffold(
        bottomBar = {
            MainBottomBar(
                isVisible = navigator.showBottomBar(),
                tabs = MainTab.entries.toImmutableList(),
                currentTab = navigator.currentTab,
                onTabSelected = navigator::navigate,
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState) {
                CommonSnackBar(
                    message = it.visuals.message,
                    contentPadding = PaddingValues(horizontal = 17.dp, vertical = 15.dp),
                    textStyle = NapzakMarketTheme.typography.caption12m.copy(
                        color = NapzakMarketTheme.colors.white,
                        textAlign = TextAlign.Center,
                    )
                )
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        CompositionLocalProvider(
            LocalSnackBarController provides snackBarController
        ) {
            Box {
                MainRegisterDialog(
                    visibility = navigator.isRegister,
                    onSellRegisterClick = {
                        navigator.navController.navigateToSaleRegistration()
                        navigator.dismissRegisterDialog()
                    },
                    onBuyRegisterClick = {
                        navigator.navController.navigateToPurchaseRegistration()
                        navigator.dismissRegisterDialog()
                    },
                    onDismissRequest = navigator::dismissRegisterDialog,
                    modifier = Modifier
                        .padding(innerPadding)
                        .zIndex(1f),
                )

                MainNavHost(
                    navigator = navigator,
                    modifier = Modifier.padding(innerPadding),
                )
            }
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

        onboardingGraph(
            navController = navigator.navController,
            onFinish = {
                navigator.navController.navigate(Home) {
                    popUpTo(Terms) { inclusive = true }
                }
            },
            modifier = modifier,
        )

        homeGraph(
            navigateToSearch = { navigator.navController.navigateToSearch() },
            navigateToProductDetail = { navigator.navController.navigateToProductDetail(it) },
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
            navigateToStoreReport = { /* TODO: move to store report */ },
            modifier = modifier,
        )

        productDetailGraph(
            onMarketNavigate = { navigator.navController.navigateToStore(it) }, //TODO: 내마켓 화면으로 이동
            onChatNavigate = {}, //TODO: 채팅 화면으로 이동
            onModifyNavigate = {}, //TODO: 물품 정보 수정 화면으로 이동
            onReportNavigate = { navigator.navController.navigateToProductReport(it) }, //TODO: 물품 신고 화면으로 이동
            onNavigateUp = navigator::navigateUp,
            modifier = Modifier.systemBarsPadding()
        )

        reportGraph(
            navigateUp = navigator::navigateUp,
            modifier = Modifier.systemBarsPadding()
        )

        registrationGraph(
            navigateToUp = navigator::navigateUp,
            navigateToDetail = {}, // TODO: 물품 상세 화면으로 이동
            navigateToGenreSearch = navigator.navController::navigateToGenreSearch,
            modifier = modifier,
        )

        mypageGraph(
            navigateToMyMarket = { /* TODO: 내 마켓 화면으로 이동 */ },
            navigateToSales = { /* TODO: 판매내역 화면으로 이동 */ },
            navigateToPurchase = { /* TODO: 구매내역 화면으로 이동 */ },
            navigateToRecent = { /* TODO: 최근 본 상품 화면으로 이동 */ },
            navigateToFavorite = { /* TODO: 찜 화면으로 이동 */ },
            navigateToSettings = { /* TODO: 설정 화면으로 이동 */ },
            navigateToHelp = { /* TODO: 고객센터 화면으로 이동 */ },
            modifier = modifier,
        )
    }
}
