package com.napzak.market.registration.component

import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_close_24
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.util.android.noRippleClickable

@Composable
fun RegistrationTopBar(
    title: String,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shadowColor = NapzakMarketTheme.colors.black.copy(alpha = .075f)

    Box(
        modifier = modifier
            .drawBehind {
                drawIntoCanvas { canvas ->
                    val paint = Paint().apply {
                        shader = LinearGradient(
                            0f, size.height - 2.dp.toPx(),
                            0f, size.height,
                            intArrayOf(
                                shadowColor.copy(alpha = .075f).toArgb(),
                                shadowColor.copy(alpha = 0.0f).toArgb(),
                            ),
                            null,
                            Shader.TileMode.CLAMP,
                        )
                        asFrameworkPaint().apply {
                            setShadowLayer(
                                6f,
                                0f,
                                1f,
                                shadowColor.toArgb()
                            )
                        }
                    }
                    val shadowHeight = 2.dp.toPx()

                    canvas.drawRect(
                        left = 0f,
                        top = size.height - shadowHeight,
                        right = size.width,
                        bottom = size.height,
                        paint = paint,
                    )
                }
            }
            .padding(top = 32.dp, end = 12.dp, bottom = 20.dp),
        ) {
        Text(
            text = title,
            style = NapzakMarketTheme.typography.body16b.copy(
                color = NapzakMarketTheme.colors.gray500,
            ),
            modifier = Modifier.align(Alignment.Center)
        )
        Icon(
            imageVector = ImageVector.vectorResource(ic_close_24),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .noRippleClickable(onCloseClick),
            tint = Color.Unspecified,
        )
    }
}

@Preview
@Composable
private fun RegistrationTopBarPreview() {
    NapzakMarketTheme {
        RegistrationTopBar(
            title = "팔아요 등록",
            onCloseClick = { },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
