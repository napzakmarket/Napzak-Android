package com.napzak.market.util.android

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.napzak.market.util.android.model.ShadowDirection

/**
 * 컴포저블의 상/하/좌/우단에 자연스러운 그라데이션 그림자를 추가하는 Modifier 확장 함수입니다.
 *
 * @param height 그림자 높이(dp). 그림자가 퍼지는 범위를 조절합니다.
 * @param startColor 그림자의 시작 색상. 일반적으로 투명도 있는 블랙 계열을 사용합니다.
 * @param endColor 그림자의 끝 색상. 보통 Color.Transparent로 자연스러운 fade-out 효과를 줍니다.
 * @param direction 그림자 방향 (Top 또는 Bottom). 현재는 수직 방향만 지원합니다.
 *
 */

fun Modifier.napzakGradientShadow(
    height: Dp,
    startColor: Color,
    endColor: Color,
    direction: ShadowDirection,
): Modifier = this.then(
    Modifier.drawWithContent {
        drawContent()

        val shadowHeightPx = height.toPx()
        val gradientBrush = Brush.verticalGradient(
            colors = listOf(startColor, endColor)
        )

        when (direction) {
            ShadowDirection.Bottom -> drawRect(
                brush = gradientBrush,
                topLeft = Offset(0f, size.height - shadowHeightPx),
                size = Size(size.width, shadowHeightPx),
            )

            ShadowDirection.Top -> drawRect(
                brush = gradientBrush,
                topLeft = Offset(0f, 0f),
                size = Size(size.width, shadowHeightPx),
            )

            else -> Unit
        }
    }
)