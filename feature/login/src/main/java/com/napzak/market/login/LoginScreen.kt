package com.napzak.market.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun LoginRoute(
    onKakaoLoginClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    LoginScreen(
        onKakaoLoginClick = onKakaoLoginClick,
    )
}

@Composable
private fun LoginScreen(
    onKakaoLoginClick: () -> Unit = {},
){
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(lottie_login)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NapzakMarketTheme.colors.white)
            .padding(horizontal = 20.dp)
            .padding(
                bottom = WindowInsets.navigationBars
                    .asPaddingValues()
                    .calculateBottomPadding()
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )

        KakaoLoginButton(onClick = onKakaoLoginClick)

        Spacer(modifier = Modifier.height(20.dp))
    }
}