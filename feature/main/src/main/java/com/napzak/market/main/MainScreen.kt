package com.napzak.market.main

import android.app.Activity
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.napzak.market.chat.navigation.chatGraph
import com.napzak.market.chat.navigation.navigateToChatRoom
import com.napzak.market.common.type.SortType
import com.napzak.market.common.type.TradeType
import com.napzak.market.designsystem.component.toast.LocalNapzakToast
import com.napzak.market.designsystem.component.toast.NapzakToast
import com.napzak.market.designsystem.component.toast.ToastType
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.detail.navigation.navigateToProductDetail
import com.napzak.market.detail.navigation.productDetailGraph
import com.napzak.market.explore.navigation.exploreGraph
import com.napzak.market.explore.navigation.navigateToExplore
import com.napzak.market.explore.navigation.navigateToGenreDetail
import com.napzak.market.home.navigation.Home
import com.napzak.market.home.navigation.homeGraph
import com.napzak.market.home.navigation.navigateToHome
import com.napzak.market.login.navigation.Login
import com.napzak.market.login.navigation.loginGraph
import com.napzak.market.login.navigation.navigateToLogin
import com.napzak.market.main.R.string.main_snack_bar_finish
import com.napzak.market.main.component.MainBottomBar
import com.napzak.market.main.component.MainRegisterDialog
import com.napzak.market.mypage.navigation.mypageGraph
import com.napzak.market.mypage.navigation.navigateToSettings
import com.napzak.market.onboarding.navigation.Terms
import com.napzak.market.onboarding.navigation.onboardingGraph
import com.napzak.market.registration.navigation.navigateToPurchaseRegistration
import com.napzak.market.registration.navigation.navigateToSaleRegistration
import com.napzak.market.registration.navigation.registrationGraph
import com.napzak.market.report.navigation.navigateToProductReport
import com.napzak.market.report.navigation.navigateToUserReport
import com.napzak.market.report.navigation.reportGraph
import com.napzak.market.search.navigation.Search
import com.napzak.market.search.navigation.navigateToSearch
import com.napzak.market.search.navigation.searchGraph
import com.napzak.market.splash.navigation.Splash
import com.napzak.market.splash.navigation.splashGraph
import com.napzak.market.store.edit_store.navigation.editStoreGraph
import com.napzak.market.store.edit_store.navigation.navigateToEditStore
import com.napzak.market.store.store.navigation.navigateToStore
import com.napzak.market.store.store.navigation.storeGraph
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MainScreen(
    restartApplication: () -> Unit,
    connectSocket: () -> Unit,
    navigator: MainNavigator = rememberMainNavigator(),
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val napzakToast = remember { NapzakToast(context, lifecycleOwner) }
    var backPressedTime by remember { mutableLongStateOf(0) }
    val mainBackHandlerEvent = remember(backPressedTime) {
        {
            if (navigator.isRegister) {
                navigator.dismissRegisterDialog()
            } else {
                if (System.currentTimeMillis() - backPressedTime <= 3000) {
                    (context as Activity).finish()
                } else {
                    napzakToast.makeText(
                        message = context.getString(main_snack_bar_finish),
                        yOffset = napzakToast.toastOffsetWithBottomBar(),
                        toastType = ToastType.COMMON,
                    )
                }
                backPressedTime = System.currentTimeMillis()
            }
        }
    }

    LaunchedEffect(Unit) {
        ChatDeepLinkEventBus.events.collect { event ->
            when (event) {
                is ChatDeepLinkEvent.ChatRoom -> {
                    val currentRoute = navigator.navController.currentBackStackEntry?.destination

                    if (currentRoute != Login) {
                        navigator.navigate(MainTab.CHAT)
                        val id = event.chatRoomId
                        id?.let { navigator.navController.navigateToChatRoom(id.toLong()) }
                    }
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            MainBottomBar(
                isVisible = navigator.showBottomBar(),
                tabs = MainTab.entries.toImmutableList(),
                currentTab = navigator.currentTab,
                onTabSelected = navigator::navigate,
            )
        },
        containerColor = NapzakMarketTheme.colors.white,
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        CompositionLocalProvider(
            LocalNapzakToast provides napzakToast
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
                    onDismissRequest = navigator::navigateUp,
                    modifier = Modifier
                        .padding(innerPadding)
                        .zIndex(1f),
                )

                MainNavHost(
                    navigator = navigator,
                    restartApplication = restartApplication,
                    endApplicationAtHome = mainBackHandlerEvent,
                    connectSocket = connectSocket,
                    innerPadding = innerPadding,
                    modifier = Modifier,
                )
            }
        }
    }
}

@Composable
private fun MainNavHost(
    navigator: MainNavigator,
    restartApplication: () -> Unit,
    endApplicationAtHome: () -> Unit,
    connectSocket: () -> Unit,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val bottomPadding = innerPadding.calculateBottomPadding()

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
        modifier = modifier.fillMaxSize(),
        navController = navigator.navController,
        startDestination = navigator.startDestination,
    ) {
        splashGraph(
            onNavigateToMain = {
                connectSocket()
                navigator.navController.navigateToHome(
                    navOptions {
                        popUpTo<Splash> { inclusive = true }
                        launchSingleTop = true
                    }
                )
            },
            onNavigateToLogin = {
                navigator.navController.navigateToLogin(
                    navOptions {
                        popUpTo<Splash> { inclusive = true }
                        launchSingleTop = true
                    }
                )
            },
        )

        loginGraph(
            onNavigateToTerms = { navigator.navController.navigate(Terms) },
            onNavigateToHome = {
                connectSocket()
                navigator.navController.navigate(Home)
            },
            modifier = modifier
                .padding(bottom = bottomPadding),
        )

        onboardingGraph(
            navController = navigator.navController,
            onFinish = {
                connectSocket()
                navigator.navController.navigate(Home) {
                    popUpTo(Terms) { inclusive = true }
                }
            },
            onLogin = {
                navigator.navController.navigate(Login) {
                    popUpTo(Terms) { inclusive = true }
                }
            },
        )

        homeGraph(
            navigateToUp = endApplicationAtHome,
            navigateToSearch = navigator.navController::navigateToSearch,
            navigateToProductDetail = navigator.navController::navigateToProductDetail,
            navigateToExploreSell = {
                navigator.navController.navigateToExplore(
                    tradeType = TradeType.SELL,
                    sortType = SortType.POPULAR,
                )
            },
            navigateToExploreBuy = {
                navigator.navController.navigateToExplore(
                    tradeType = TradeType.BUY,
                    sortType = SortType.POPULAR,
                )
            },
            checkSessionManager = {
                if (SessionManager.chatRoomId != null) {
                    navigator.navigate(MainTab.CHAT)
                    navigator.navController.navigateToChatRoom(SessionManager.chatRoomId)
                    SessionManager.clearChatRoomId()
                }
            },
            modifier = modifier
                .padding(bottom = bottomPadding),
        )

        exploreGraph(
            navigateToUp = navigator::navigateUp,
            navigateToHome = { navigator.navController.popBackStack(Home, inclusive = false) },
            navigateToSearch = navigator.navController::navigateToSearch,
            navigateToGenreDetail = navigator.navController::navigateToGenreDetail,
            navigateToProductDetail = navigator.navController::navigateToProductDetail,
            modifier = modifier
                .padding(innerPadding),
        )

        searchGraph(
            navigateToPrevious = navigator::navigateUp,
            navigateToSearchResult = { searchTerm ->
                navigator.navController.popBackStack(Search, inclusive = true)
                navigator.navController.navigateToExplore(searchTerm)
            },
            navigateToGenreDetail = navigator.navController::navigateToGenreDetail,
            modifier = modifier
                .padding(innerPadding),
        )

        storeGraph(
            navigateToUp = navigator::navigateUp,
            navigateToProfileEdit = navigator.navController::navigateToEditStore,
            navigateToProductDetail = navigator.navController::navigateToProductDetail,
            navigateToStoreReport = navigator.navController::navigateToUserReport,
        )

        editStoreGraph(
            navigateToUp = navigator::navigateUp,
        )

        productDetailGraph(
            onMarketNavigate = navigator.navController::navigateToStore,
            onChatNavigate = {
                navigator.navController.navigateToChatRoom(productId = it)
            },
            onModifyNavigate = { productId, tradeType ->
                when (tradeType) {
                    TradeType.SELL -> navigator.navController.navigateToSaleRegistration(productId = productId)
                    TradeType.BUY -> navigator.navController.navigateToPurchaseRegistration(
                        productId = productId
                    )

                    else -> {}
                }
            },
            onReportNavigate = navigator.navController::navigateToProductReport,
            onNavigateUp = navigator::navigateUp,
            modifier = modifier
                .padding(innerPadding),
        )

        reportGraph(
            navigateUp = navigator::navigateUp,
        )

        registrationGraph(
            navController = navigator.navController,
            navigateToUp = navigator::navigateUp,
            navigateToDetail = { productId ->
                val navOptions = navOptions {
                    launchSingleTop = true

                    navigator.navController.currentDestination?.route?.let {
                        popUpTo(it) {
                            inclusive = true
                        }
                    }
                }
                navigator.navController.navigateToProductDetail(productId, navOptions)
            },
        )

        mypageGraph(
            navController = navigator.navController,
            navigateToUp = navigator::navigateUp,
            navigateToMyMarket = navigator.navController::navigateToStore,
            navigateToSales = { /* TODO: 판매내역 화면으로 이동 */ },
            navigateToPurchase = { /* TODO: 구매내역 화면으로 이동 */ },
            navigateToRecent = { /* TODO: 최근 본 상품 화면으로 이동 */ },
            navigateToProductDetail = navigator.navController::navigateToProductDetail,
            restartApplication = restartApplication,
            modifier = modifier
                .padding(bottom = bottomPadding),
        )

        chatGraph(
            onChatRoomNavigate = navigator.navController::navigateToChatRoom,
            onProductDetailNavigate = navigator.navController::navigateToProductDetail,
            onStoreReportNavigate = navigator.navController::navigateToUserReport,
            onSettingsNavigate = navigator.navController::navigateToSettings,
            onNavigateUp = navigator::navigateUp,
            modifier = modifier
                .padding(innerPadding),
        )
    }
}
