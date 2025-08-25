package com.napzak.market.chat.chatroom.component.chatitem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.designsystem.R.drawable.ic_circle_zoom
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.ui_util.noRippleClickable

@Composable
internal fun ChatImageItem(
    imageUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val shape = RoundedCornerShape(12.dp)
    val borderStroke = BorderStroke(
        width = 1.dp,
        color = NapzakMarketTheme.colors.gray100,
    )
    val imageSize = 160.dp

    Box(
        modifier = modifier
            .size(imageSize)
            .border(
                border = borderStroke,
                shape = shape,
            )
            .noRippleClickable(onClick = onClick),
    ) {
        AsyncImage(
            model = ImageRequest
                .Builder(context)
                .data(imageUrl)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(shape)
                .fillMaxSize(),
        )

        Icon(
            imageVector = ImageVector.vectorResource(ic_circle_zoom),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatImagePreview() {
    NapzakMarketTheme {
        Box {
            ChatImageItem(
                imageUrl = "",
                onClick = {},
                modifier = Modifier.padding(10.dp),
            )
        }
    }
}
