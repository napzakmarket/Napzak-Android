package com.napzak.market.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.napzak.market.designsystem.R.drawable.ic_logo
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import kotlinx.coroutines.delay

@Composable
fun SplashRoute(
    onNavigateToMain: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val systemUiController = rememberSystemUiController()
    val splashColor = NapzakMarketTheme.colors.purple500
    val originalColor = NapzakMarketTheme.colors.white
    val originalDarkIcons = true

    var isSuccess by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        systemUiController.setSystemBarsColor(color = splashColor, darkIcons = false)
        onDispose {
            systemUiController.setSystemBarsColor(
                color = originalColor,
                darkIcons = originalDarkIcons,
            )
        }
    }

    LaunchedEffect(Unit) {
        val result = viewModel.tryAutoLogin()
        isSuccess = result.isSuccess

        delay(2500)
        if (isSuccess) {
            onNavigateToMain()
        } else {
            onNavigateToOnboarding()
        }
    }

    SplashScreen(
        modifier = modifier,
        backgroundColor = splashColor,
    )
}

@Composable
private fun SplashScreen(
    backgroundColor: Color,
    modifier: Modifier = Modifier,
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