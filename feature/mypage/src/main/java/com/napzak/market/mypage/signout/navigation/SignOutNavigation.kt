package com.napzak.market.mypage.signout.navigation

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.Route
import com.napzak.market.mypage.signout.SignOutConfirmScreen
import com.napzak.market.mypage.signout.SignOutDetailScreen
import com.napzak.market.mypage.signout.SignOutReasonScreen
import com.napzak.market.mypage.signout.SignOutSideEffect
import com.napzak.market.mypage.signout.SignOutViewModel
import com.napzak.market.ui_util.horizontalSlideNavigation
import com.napzak.market.ui_util.sharedViewModel
import kotlinx.serialization.Serializable

fun NavHostController.navigateToSignOut(
    navOptions: NavOptions? = null,
) = navigate(route = SignOut, navOptions = navOptions)

fun NavHostController.popSignOut() = popBackStack(SignOutReason, inclusive = true)

fun NavGraphBuilder.signOutGraph(
    navController: NavHostController,
    onNavigateUp: () -> Unit,
    restartApp: () -> Unit,
) {
    val modifier = Modifier.systemBarsPadding()

    horizontalSlideNavigation<SignOut, Route>(
        startDestination = SignOutReason,
        screens = listOf(SignOutReason, SignOutDetail, SignOutConfirm),
    ) {
        composable<SignOutReason> { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<SignOutViewModel>(navController)

            SignOutReasonScreen(
                onNavigateUpClick = onNavigateUp,
                onProceedClick = { navController.navigate(SignOutDetail) },
                onReasonSelect = { viewModel.signOutReason = it },
                modifier = modifier,
            )
        }

        composable<SignOutDetail> { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<SignOutViewModel>(navController)

            SignOutDetailScreen(
                onNavigateUpClick = onNavigateUp,
                onProceedClick = { detail ->
                    viewModel.signOutDescription = detail
                    navController.navigate(SignOutConfirm)
                },
                modifier = modifier,
            )
        }

        composable<SignOutConfirm> { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<SignOutViewModel>(navController)
            val lifecycleOwner = LocalLifecycleOwner.current

            LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
                viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle).collect {
                    if (it is SignOutSideEffect.SignOutComplete) {
                        restartApp()
                    }
                }
            }

            SignOutConfirmScreen(
                onConfirmClick = {
                    viewModel.proceedSignOut()
                },
                onCancelClick = navController::popSignOut,
                onNavigateUpClick = onNavigateUp,
                modifier = modifier,
            )
        }
    }
}

@Serializable
data object SignOut : Route

@Serializable
private data object SignOutReason : Route

@Serializable
private data object SignOutDetail : Route

@Serializable
private data object SignOutConfirm : Route

