package com.napzak.market.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.napzak.market.designsystem.R.raw.lottie_login
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.login.component.KakaoLoginButton
import com.napzak.market.login.model.LoginFlowRoute

@Composable
fun LoginRoute(
    onRouteChanged: (LoginFlowRoute) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.route) {
        uiState.route?.let { onRouteChanged(it) }
    }

    LoginScreen(
        onKakaoLoginClick = {
            LoginKakaoLauncher(
                context = context,
                onTokenReceived = { token -> viewModel.loginWithKakao(token) },
                onError = {
                    //TODO 추후 구현
                }
            ).startKakaoLogin()
        },
        modifier = modifier,
    )
}

@Composable
private fun LoginScreen(
    onKakaoLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(lottie_login)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NapzakMarketTheme.colors.white)
            .padding(
                horizontal = 20.dp,
                vertical = 40.dp,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(30.dp),
        )

        KakaoLoginButton(onClick = onKakaoLoginClick)
    }
}
