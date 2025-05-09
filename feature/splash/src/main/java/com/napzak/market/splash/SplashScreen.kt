package com.napzak.market.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.napzak.market.designsystem.R.drawable.ic_logo
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import kotlinx.coroutines.delay

@Composable
fun SplashRoute(
    modifier: Modifier = Modifier,
    onNavigateToOnboarding: () -> Unit,
) {
    val systemUiController = rememberSystemUiController()
    val splashColor = NapzakMarketTheme.colors.purple500
    val originalColor = Color.White
    val originalDarkIcons = true

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = splashColor,
            darkIcons = false,
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            systemUiController.setSystemBarsColor(
                color = originalColor,
                darkIcons = originalDarkIcons,
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(2500)
        onNavigateToOnboarding()
    }

    SplashScreen(
        modifier = modifier,
        backgroundColor = splashColor,
    )
}


@Composable
private fun SplashScreen(
    modifier: Modifier = Modifier,
    backgroundColor: Color
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(ic_logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1.7f),
        )
    }
}