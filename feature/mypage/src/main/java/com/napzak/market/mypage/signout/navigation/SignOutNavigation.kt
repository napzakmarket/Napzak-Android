package com.napzak.market.mypage.signout.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.Route
import com.napzak.market.mypage.signout.SignOutConfirmScreen
import com.napzak.market.mypage.signout.SignOutDetailScreen
import com.napzak.market.mypage.signout.SignOutReasonScreen
import com.napzak.market.mypage.signout.SignOutViewModel
import com.napzak.market.util.android.horizontalSlideNavigation
import com.napzak.market.util.android.sharedViewModel
import kotlinx.serialization.Serializable

fun NavHostController.navigateToSignOut(
    storeId: Long,
    navOptions: NavOptions? = null,
) = navigate(route = SignOut(storeId), navOptions = navOptions)

fun NavHostController.popSignOut() = popBackStack(SignOutReason, inclusive = true)

fun NavGraphBuilder.signOutGraph(
    navController: NavHostController,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
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

            SignOutConfirmScreen(
                onConfirmClick = {
                    viewModel.proceedSignOut()
                    navController.popSignOut()
                },
                onCancelClick = navController::popSignOut,
                onNavigateUpClick = onNavigateUp,
                modifier = modifier,
            )
        }
    }
}

@Serializable
data class SignOut(val storeId: Long) : Route

@Serializable
private data object SignOutReason : Route

@Serializable
private data object SignOutDetail : Route

@Serializable
private data object SignOutConfirm : Route

