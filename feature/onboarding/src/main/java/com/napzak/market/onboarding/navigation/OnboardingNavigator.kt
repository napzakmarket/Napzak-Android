package com.napzak.market.onboarding.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.Route
import com.napzak.market.onboarding.genre.GenreRoute
import com.napzak.market.onboarding.nickname.NicknameRoute
import com.napzak.market.onboarding.termsAgreement.TermsAgreementRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToTerms(navOptions: NavOptions? = null) =
    navigate(Terms, navOptions)

fun NavController.navigateToNickname(navOptions: NavOptions? = null) =
    navigate(Nickname, navOptions)

fun NavController.navigateToGenre(navOptions: NavOptions? = null) =
    navigate(Genre, navOptions)

fun NavGraphBuilder.onboardingGraph(
    onFinish: () -> Unit,
    onLogin: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    composable<Terms> {
        TermsAgreementRoute(
            onBackClick = onLogin,
            onNextClick = navController::navigateToNickname,
        )
    }

    composable<Nickname> {
        NicknameRoute(
            onBackClick = navController::navigateToTerms,
            onNextClick = navController::navigateToGenre,
        )
    }

    composable<Genre> {
        GenreRoute(
            onBackClick = navController::navigateUp,
            onDoneClick = onFinish,
            onSkipClick = onFinish,
        )
    }

}

@Serializable
object Terms : Route

@Serializable
object Nickname : Route

@Serializable
object Genre : Route