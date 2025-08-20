package com.napzak.market.splash.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.Route
import com.napzak.market.splash.SplashRoute
import kotlinx.serialization.Serializable

fun NavGraphBuilder.splashGraph(
    onNavigateToMain: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    modifier: Modifier = Modifier,
) {
    composable<Splash> {
        SplashRoute(
            onNavigateToMain = onNavigateToMain,
            onNavigateToOnboarding = onNavigateToOnboarding,
            modifier = modifier,
        )
    }
}

@Serializable
object Splash : Route
