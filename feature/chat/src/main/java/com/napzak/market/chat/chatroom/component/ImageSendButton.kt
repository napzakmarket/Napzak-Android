package com.napzak.market.chat.chatroom.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_gray_send
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.chat.R.string.chat_room_input_field_send
import com.napzak.market.ui_util.noRippleClickable

@Composable
internal fun ImageSendButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val contentColor = NapzakMarketTheme.colors.white
    val containerColor = NapzakMarketTheme.colors.purple500
    val shape = RoundedCornerShape(32.dp)

    Row(
        modifier = modifier
            .noRippleClickable(onClick = onClick)
            .background(color = containerColor, shape = shape)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(chat_room_input_field_send),
            style = NapzakMarketTheme.typography.body14b,
            color = contentColor,
        )

        Icon(
            imageVector = ImageVector.vectorResource(ic_gray_send),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(16.dp),
        )
    }
}

@Preview
@Composable
private fun ImageSendButtonPreview() {
    NapzakMarketTheme {
        ImageSendButton(
            onClick = {},
        )
    }
}