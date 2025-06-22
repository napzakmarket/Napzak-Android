package com.napzak.market.mypage.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.MainTabRoute
import com.napzak.market.common.navigation.Route
import com.napzak.market.mypage.mypage.MyPageRoute
import com.napzak.market.mypage.setting.SettingsRoute
import com.napzak.market.mypage.withdraw.WithdrawConfirmScreen
import com.napzak.market.mypage.withdraw.WithdrawDetailScreen
import com.napzak.market.mypage.withdraw.WithdrawReasonScreen
import com.napzak.market.mypage.withdraw.WithdrawSideEffect
import com.napzak.market.mypage.withdraw.WithdrawViewModel
import com.napzak.market.ui_util.horizontalSlideNavigation
import com.napzak.market.ui_util.sharedViewModel
import kotlinx.serialization.Serializable

fun NavController.navigateToMyPage(navOptions: NavOptions? = null) {
    this.navigate(
        route = MyPage,
        navOptions = navOptions,
    )
}

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    this.navigate(
        route = Settings,
        navOptions = navOptions,
    )
}

fun NavHostController.navigateToWithdraw(navOptions: NavOptions? = null) {
    this.navigate(
        route = Withdraw,
        navOptions = navOptions,
    )
}

fun NavHostController.navigateToWithdrawDetail(navOptions: NavOptions? = null) {
    this.navigate(
        route = WithdrawDetail,
        navOptions = navOptions,
    )
}

fun NavHostController.navigateToWithdrawConfirm(navOptions: NavOptions? = null) {
    this.navigate(
        route = WithdrawConfirm,
        navOptions = navOptions,
    )
}

fun NavHostController.popBackStackOnCompleteWithdraw() {
    this.popBackStack(
        route = Withdraw,
        inclusive = true,
    )
}

fun NavGraphBuilder.mypageGraph(
    navController: NavHostController,
    restartApplication: () -> Unit,
    navigateToUp: () -> Unit,
    navigateToMyMarket: (Long) -> Unit,
    navigateToSales: () -> Unit,
    navigateToPurchase: () -> Unit,
    navigateToRecent: () -> Unit,
    navigateToWishlist: () -> Unit,
    modifier: Modifier = Modifier,
) {
    composable<MyPage> {
        BackHandler {
            navigateToUp()
        }

        MyPageRoute(
            onMyMarketClick = navigateToMyMarket,
            onSalesClick = navigateToSales,
            onPurchaseClick = navigateToPurchase,
            onRecentClick = navigateToRecent,
            onFavoriteClick = navigateToWishlist,
            onSettingsClick = navController::navigateToSettings,
            modifier = modifier,
        )
    }

    composable<Settings> {
        SettingsRoute(
            onBackClick = navigateToUp,
            onLogoutConfirm = restartApplication,
            onWithdrawClick = navController::navigateToWithdraw,
        )
    }

    horizontalSlideNavigation<Withdraw>(
        startDestination = WithdrawReason,
    ) {
        val systemBarPaddingModifier = Modifier.systemBarsPadding()

        composable<WithdrawReason> { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<WithdrawViewModel>(navController)

            WithdrawReasonScreen(
                onNavigateUpClick = navigateToUp,
                onProceedClick = navController::navigateToWithdrawDetail,
                onReasonSelect = { viewModel.withdrawReason = it },
                modifier = systemBarPaddingModifier,
            )
        }

        composable<WithdrawDetail> { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<WithdrawViewModel>(navController)

            WithdrawDetailScreen(
                onNavigateUpClick = navigateToUp,
                onProceedClick = { detail ->
                    viewModel.withdrawDescription = detail
                    navController.navigateToWithdrawConfirm()
                },
                modifier = systemBarPaddingModifier,
            )
        }

        composable<WithdrawConfirm> { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<WithdrawViewModel>(navController)
            val lifecycleOwner = LocalLifecycleOwner.current

            LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
                viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle).collect {
                    if (it is WithdrawSideEffect.WithdrawComplete) {
                        restartApplication()
                    }
                }
            }

            WithdrawConfirmScreen(
                onConfirmClick = viewModel::withdrawStore,
                onCancelClick = navController::popBackStackOnCompleteWithdraw,
                onNavigateUpClick = navigateToUp,
                modifier = systemBarPaddingModifier,
            )
        }
    }
}

@Serializable
data object MyPage : MainTabRoute

@Serializable
data object Settings : Route

@Serializable
data object Withdraw : Route

@Serializable
private data object WithdrawReason : Route

@Serializable
private data object WithdrawDetail : Route

@Serializable
private data object WithdrawConfirm : Route