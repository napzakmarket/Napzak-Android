package com.napzak.market.designsystem.component.image

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.designsystem.R.drawable.ic_gray_arrow_left
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.ui_util.noRippleClickable
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ZoomableImageScreen(
    imageUrls: ImmutableList<String>,
    initialPage: Int,
    contentDescription: String?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(initialPage = initialPage) { imageUrls.size }
    var layoutSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = NapzakMarketTheme.colors.black)
            .zIndex(1f)
            .onGloballyPositioned { layoutSize = it.size },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .padding(start = 13.dp, top = 34.dp)
                .size(24.dp)
                .align(Alignment.TopStart)
                .zIndex(1f)
                .noRippleClickable(onClick = onBackClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(ic_gray_arrow_left),
                contentDescription = null,
                tint = NapzakMarketTheme.colors.white,
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize(),
        ) { page ->
            ZoomableImage(
                imageUrl = imageUrls[page],
                contentDescription = contentDescription,
                parentLayoutSize = layoutSize,
            )
        }
    }
}

@Composable
private fun ZoomableImage(
    imageUrl: String,
    contentDescription: String?,
    parentLayoutSize: IntSize,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val zoomState = rememberZoomState()

    LaunchedEffect(parentLayoutSize) { zoomState.parentLayoutSize = parentLayoutSize }

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .fillMaxSize()
            .zoomable(zoomState)
    )
}

// TODO: 안정화되면 ui_util로 이동
private fun Modifier.zoomable(
    zoomState: ZoomState,
) = this
    .onGloballyPositioned {
        zoomState.originalIntSize = it.size
    }
    .pointerInput(Unit) {
        awaitEachGesture {
            awaitFirstDown(requireUnconsumed = false)

            do {
                val event = awaitPointerEvent()
                val zoomChange = event.calculateZoom()
                val panChange = event.calculatePan()
                var newOffset = Offset.Zero

                zoomState.updateScale(zoomChange)
                if (zoomState.scale > 1.0f) {
                    newOffset = zoomState.getConstrainedOffset(panChange)
                    event.changes.forEach { it.consume() }
                }
                zoomState.updateOffset(newOffset)
            } while (event.changes.any { it.pressed })
        }
    }
    .graphicsLayer(
        scaleX = zoomState.scale,
        scaleY = zoomState.scale,
        translationX = zoomState.offset.x,
        translationY = zoomState.offset.y,
    )


@Composable
private fun rememberZoomState(
    scaleRange: ClosedFloatingPointRange<Float> = 1f..5f,
) = remember { ZoomState(scaleRange) }

@Stable
private class ZoomState(
    private val scaleRange: ClosedFloatingPointRange<Float>,
) {
    var scale by mutableFloatStateOf(1f)
        private set
    var offset by mutableStateOf(Offset.Zero)
        private set

    var parentLayoutSize by mutableStateOf(IntSize.Zero)
    var originalIntSize by mutableStateOf(IntSize.Zero)

    private val offsetConstraints = derivedStateOf {
        Offset(
            x = (originalIntSize.width * scale - parentLayoutSize.width) / 2f,
            y = (originalIntSize.height * scale - parentLayoutSize.height) / 2f,
        )
    }

    fun updateScale(zoomChange: Float) {
        this.scale = (this.scale * zoomChange).coerceIn(scaleRange)
    }

    fun updateOffset(newOffset: Offset) {
        offset = newOffset
    }

    fun getConstrainedOffset(panChange: Offset): Offset {
        fun calculateConstrainedOffset(
            currentOffset: Float,
            panChange: Float,
            scale: Float,
            originalSize: Int,
            parentSize: Int,
            constraint: Float,
        ): Float {
            return when {
                scale * originalSize < parentSize -> 0f
                currentOffset + panChange !in -constraint..constraint -> {
                    currentOffset.coerceIn(-constraint, constraint)
                }

                else -> currentOffset + panChange * scale
            }
        }

        val offsetX = calculateConstrainedOffset(
            currentOffset = offset.x,
            panChange = panChange.x,
            scale = scale,
            originalSize = originalIntSize.width,
            parentSize = parentLayoutSize.width,
            constraint = offsetConstraints.value.x,
        )

        val offsetY = calculateConstrainedOffset(
            currentOffset = offset.y,
            panChange = panChange.y,
            scale = scale,
            originalSize = originalIntSize.height,
            parentSize = parentLayoutSize.height,
            constraint = offsetConstraints.value.y,
        )

        return Offset(x = offsetX, y = offsetY)
    }
}
