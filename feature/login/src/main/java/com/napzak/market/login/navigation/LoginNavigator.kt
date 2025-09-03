package com.napzak.market.login.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.Route
import com.napzak.market.login.LoginRoute
import com.napzak.market.login.model.LoginFlowRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToLogin(navOptions: NavOptions? = null) = navigate(Login, navOptions)

fun NavGraphBuilder.loginGraph(
    onNavigateToTerms: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    composable<Login> {
        LoginRoute(
            onRouteChanged = { route ->
                when (route) {
                    LoginFlowRoute.Terms -> onNavigateToTerms()
                    LoginFlowRoute.Main -> onNavigateToHome()
                    LoginFlowRoute.Reported -> { } /* 화면 이동 없이 팝업만 동작 */
                }
            },
            modifier = modifier,
        )
    }
}

@Serializable
object Login : Route