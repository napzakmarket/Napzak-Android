package com.napzak.market.splash

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.napzak.market.designsystem.R.drawable.ic_logo
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import kotlinx.coroutines.delay

@Composable
internal fun SplashRoute(
    onNavigateToMain: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val view = LocalView.current

    DisposableEffect(Unit) {
        val window = (view.context as Activity).window
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        onDispose {
            controller.isAppearanceLightStatusBars = true
            controller.isAppearanceLightNavigationBars = true
        }
    }

    val navigated = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val success = viewModel.tryAutoLogin().isSuccess
        delay(2500)
        if (!navigated.value) {
            navigated.value = true
            if (success) onNavigateToMain() else onNavigateToLogin()
        }
    }

    SplashScreen(
        modifier = modifier,
    )
}

@Composable
private fun SplashScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NapzakMarketTheme.colors.purple500),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(ic_logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1.7f),
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    NapzakMarketTheme {
        SplashScreen()
    }
}
