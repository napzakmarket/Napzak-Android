package com.napzak.market.login.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.Route
import com.napzak.market.login.LoginRoute
import kotlinx.serialization.Serializable

fun NavGraphBuilder.loginGraph(
    onKakaoLoginClick: () -> Unit,
    navController: NavHostController,
    ) {
    composable<Login> {
        LoginRoute(onKakaoLoginClick = onKakaoLoginClick)
    }
}

@Serializable
object Login : Route