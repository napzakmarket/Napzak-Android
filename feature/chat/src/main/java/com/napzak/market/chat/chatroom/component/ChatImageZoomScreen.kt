package com.napzak.market.chat.chatroom.component

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.napzak.market.designsystem.R.drawable.ic_arrow_left
import com.napzak.market.designsystem.R.drawable.ic_close_24
import com.napzak.market.designsystem.component.image.ZoomableImage
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.ui_util.noRippleClickable

@Composable
internal fun ChatImageZoomScreen(
    selectedImageUrl: String,
    isPreview: Boolean,
    onBackClick: () -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    var layoutSize by remember { mutableStateOf(IntSize.Zero) }

    BackHandler {
        onBackClick()
    }

    DisposableEffect(selectedImageUrl) {
        focusManager.clearFocus()

        val window = (context as? Activity)?.window
        val windowInsetsController =
            window?.let { WindowCompat.getInsetsController(window, window.decorView) }

        windowInsetsController?.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsController?.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        onDispose {
            windowInsetsController?.show(WindowInsetsCompat.Type.systemBars())
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = NapzakMarketTheme.colors.black)
            .onGloballyPositioned { layoutSize = it.size }
    ) {
        ZoomableImage(
            imageUrl = selectedImageUrl,
            contentDescription = null,
            parentLayoutSize = layoutSize,
            modifier = Modifier
                .fillMaxSize()
        )

        if (isPreview) {
            PreviewImageZoomTopBar(
                onSendClick = onSendClick,
                onBackClick = onBackClick,
            )
        } else {
            ImageZoomCloseButton(
                onBackClick = onBackClick,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
    }
}

@Composable
private fun PreviewImageZoomTopBar(
    onSendClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 53.dp)
            .zIndex(1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .noRippleClickable(onClick = onBackClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(ic_arrow_left),
                contentDescription = null,
                tint = NapzakMarketTheme.colors.gray200,
            )
        }

        ImageSendButton(onClick = onSendClick)
    }
}

@Composable
private fun ImageZoomCloseButton(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(end = 20.dp, top = 58.dp)
            .size(24.dp)
            .noRippleClickable(onClick = onBackClick)
            .zIndex(1f),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(ic_close_24),
            contentDescription = null,
            tint = NapzakMarketTheme.colors.gray200,
        )
    }
}