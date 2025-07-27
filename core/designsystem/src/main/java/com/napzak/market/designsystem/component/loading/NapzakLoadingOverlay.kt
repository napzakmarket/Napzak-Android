package com.napzak.market.designsystem.component.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.napzak.market.designsystem.R.raw.lottie_napzak
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.ui_util.ScreenPreview

@Composable
fun NapzakLoadingOverlay(
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottie_napzak))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        clipSpec = LottieClipSpec.Progress(0f, 0.9f)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NapzakMarketTheme.colors.white)
            .zIndex(1f),
        contentAlignment = Alignment.Center,
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
        )
    }
}

@ScreenPreview
@Composable
private fun NapzakLoadingOverlayPreview() {
    NapzakMarketTheme {
        NapzakLoadingOverlay()
    }
}
