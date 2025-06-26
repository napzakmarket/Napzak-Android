package com.napzak.market.designsystem.component.image

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.designsystem.R.drawable.ic_arrow_left
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.ui_util.noRippleClickable

@Composable
fun ZoomableImageScreen(
    imageRequest: ImageRequest,
    contentDescription: String?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var rootSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = NapzakMarketTheme.colors.black)
            .onGloballyPositioned { rootSize = it.size },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(ic_arrow_left),
            contentDescription = null,
            tint = NapzakMarketTheme.colors.white,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 20.dp, top = 34.dp)
                .noRippleClickable(onClick = onBackClick),
        )

        AsyncImage(
            model = imageRequest,
            contentDescription = contentDescription,
            modifier = Modifier.zoomable(rootSize),
        )
    }
}

// TODO: 안정화되면 ui_util로 이동
private fun Modifier.zoomable(
    parentLayoutSize: IntSize,
    scaleRange: ClosedFloatingPointRange<Float> = 1f..5f,
) = composed {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var originalIntSize by remember { mutableStateOf(IntSize.Zero) }

    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale = (scale * zoomChange).coerceIn(scaleRange)
        val offsetConstraints = Offset(
            x = (originalIntSize.width * scale - parentLayoutSize.width) / 2f,
            y = (originalIntSize.height * scale - parentLayoutSize.height) / 2f,
        )

        val offsetX = when {
            scale * originalIntSize.width < parentLayoutSize.width -> 0f

            offset.x + offsetChange.x !in -offsetConstraints.x..offsetConstraints.x -> {
                offset.x.coerceIn(
                    minimumValue = -offsetConstraints.x,
                    maximumValue = offsetConstraints.x,
                )
            }

            else -> offset.x + offsetChange.x * scale
        }
        val offsetY = when {
            scale * originalIntSize.height < parentLayoutSize.height -> 0f

            offset.y + offsetChange.y !in -offsetConstraints.y..offsetConstraints.y -> {
                offset.y.coerceIn(
                    minimumValue = -offsetConstraints.y,
                    maximumValue = offsetConstraints.y,
                )
            }

            else -> offset.y + offsetChange.y * scale
        }
        offset = Offset(x = offsetX, y = offsetY)
    }

    this
        .onGloballyPositioned {
            originalIntSize = it.size
        }
        .graphicsLayer(
            scaleX = scale,
            scaleY = scale,
            translationX = offset.x,
            translationY = offset.y,
        )
        .transformable(state = state)
}
