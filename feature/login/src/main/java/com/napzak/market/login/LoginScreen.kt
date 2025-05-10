package com.napzak.market.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.airbnb.lottie.compose.*

@Composable
private fun LoginScreen(
    onKakaoLoginClick: () -> Unit = {},
){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(com.napzak.market.designsystem.R.raw.lottie_login))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}