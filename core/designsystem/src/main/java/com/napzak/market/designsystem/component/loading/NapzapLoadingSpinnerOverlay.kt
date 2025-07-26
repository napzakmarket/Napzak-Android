package com.napzak.market.designsystem.component.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.napzak.market.designsystem.R.raw.lottie_spinner

/**
 * 스피너 로딩 뷰
 * 각 화면마다 배경색을 적용해서 사용할 수 있도록 별도로 배경색을 설정하지 않았습니다.
 *
 * @param modifier 수정자
 */
@Composable
fun NapzakLoadingSpinnerOverlay(
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottie_spinner))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        clipSpec = LottieClipSpec.Progress(0f, 0.9f)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
            .zIndex(1f),
        contentAlignment = Alignment.Center,
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
        )
    }
}
