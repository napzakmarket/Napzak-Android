package com.napzak.market.detail.component.bottombar

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_red_heart
import com.napzak.market.designsystem.R.drawable.ic_white_arrow_right
import com.napzak.market.designsystem.R.drawable.ic_white_heart
import com.napzak.market.designsystem.component.button.NapzakButton
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.detail.R.string.detail_product_button_chat

@Composable
internal fun ProductDetailBottomBar(
    isLiked: Boolean,
    onChatButtonClick: () -> Unit,
    onLikeButtonClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shadowElevation = 2.dp,
        color = NapzakMarketTheme.colors.white,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
        ) {
            LikeButton(
                isLiked = isLiked,
                onClick = { onLikeButtonClick(!isLiked) },
            )

            Spacer(Modifier.width(8.dp))

            ChatButton(
                onChatButtonClick = onChatButtonClick,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun LikeButton(
    isLiked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val likeIcon = if (isLiked) ic_red_heart else ic_white_heart
    val iconTint = if (isLiked) NapzakMarketTheme.colors.red else NapzakMarketTheme.colors.gray100
    val shape = RoundedCornerShape(14.dp)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .semantics(
                mergeDescendants = true,
                properties = { role = Role.Button },
            )
            .size(50.dp)
            .clip(shape)
            .border(
                width = 1.dp,
                color = NapzakMarketTheme.colors.gray50,
                shape = shape,
            )
            .clickable { onClick() },
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(likeIcon),
            tint = iconTint,
            contentDescription = null,
            modifier = Modifier.size(width = 21.dp, height = 19.dp),
        )
    }
}

@Composable
private fun ChatButton(
    onChatButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NapzakButton(
        text = stringResource(detail_product_button_chat),
        icon = ImageVector.vectorResource(ic_white_arrow_right),
        onClick = onChatButtonClick,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun ChatButtonPreview() {
    NapzakMarketTheme {
        Column(
            modifier = Modifier.padding(top = 20.dp),
        ) {
            ProductDetailBottomBar(
                isLiked = true,
                onChatButtonClick = {},
                onLikeButtonClick = {},
            )
        }
    }
}
