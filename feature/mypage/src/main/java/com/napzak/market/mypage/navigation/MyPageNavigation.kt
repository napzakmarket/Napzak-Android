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
import com.napzak.market.mypage.signout.SignOutConfirmScreen
import com.napzak.market.mypage.signout.SignOutDetailScreen
import com.napzak.market.mypage.signout.SignOutReasonScreen
import com.napzak.market.mypage.signout.SignOutSideEffect
import com.napzak.market.mypage.signout.SignOutViewModel
import com.napzak.market.util.android.horizontalSlideNavigation
import com.napzak.market.util.android.sharedViewModel
import kotlinx.serialization.Serializable

fun NavController.navigateToMyPage(navOptions: NavOptions? = null) {
    this.navigate(MyPage, navOptions)
}

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    this.navigate(Settings, navOptions)
}

fun NavHostController.navigateToSignOut(navOptions: NavOptions? = null) {
    this.navigate(route = SignOut, navOptions = navOptions)
}

fun NavGraphBuilder.mypageGraph(
    navController: NavHostController,
    restartApplication: () -> Unit,
    navigateToUp: () -> Unit,
    navigateToMyMarket: (Long) -> Unit,
    navigateToSales: () -> Unit,
    navigateToPurchase: () -> Unit,
    navigateToRecent: () -> Unit,
    navigateToFavorite: () -> Unit,
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
            onFavoriteClick = navigateToFavorite,
            onSettingsClick = navController::navigateToSettings,
            modifier = modifier,
        )
    }

    composable<Settings> {
        SettingsRoute(
            onBackClick = navigateToUp,
            onLogoutConfirm = restartApplication,
            onWithdrawClick = navController::navigateToSignOut,
        )
    }

    horizontalSlideNavigation<SignOut, Route>(
        startDestination = SignOutReason,
        screens = listOf(SignOutReason, SignOutDetail, SignOutConfirm),
    ) {
        val systemBarPaddingModifier = Modifier.systemBarsPadding()

        composable<SignOutReason> { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<SignOutViewModel>(navController)

            SignOutReasonScreen(
                onNavigateUpClick = navigateToUp,
                onProceedClick = { navController.navigate(SignOutDetail) },
                onReasonSelect = { viewModel.signOutReason = it },
                modifier = systemBarPaddingModifier,
            )
        }

        composable<SignOutDetail> { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<SignOutViewModel>(navController)

            SignOutDetailScreen(
                onNavigateUpClick = navigateToUp,
                onProceedClick = { detail ->
                    viewModel.signOutDescription = detail
                    navController.navigate(SignOutConfirm)
                },
                modifier = systemBarPaddingModifier,
            )
        }

        composable<SignOutConfirm> { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<SignOutViewModel>(navController)
            val lifecycleOwner = LocalLifecycleOwner.current

            LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
                viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle).collect {
                    if (it is SignOutSideEffect.SignOutComplete) {
                        restartApplication()
                    }
                }
            }

            SignOutConfirmScreen(
                onConfirmClick = {
                    viewModel.proceedSignOut()
                },
                onCancelClick = {
                    navController.popBackStack(SignOutReason, inclusive = true)
                },
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
data object SignOut : Route

@Serializable
private data object SignOutReason : Route

@Serializable
private data object SignOutDetail : Route

@Serializable
private data object SignOutConfirm : Route