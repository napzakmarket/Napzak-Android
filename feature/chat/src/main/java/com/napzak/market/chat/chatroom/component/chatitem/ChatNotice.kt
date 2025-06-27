package com.napzak.market.chat.chatroom.component.chatitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme

@Composable
internal fun ChatNotice(
    notice: String,
    modifier: Modifier = Modifier,
) {
    val contentColor = NapzakMarketTheme.colors.gray200

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = contentColor,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = notice,
            style = NapzakMarketTheme.typography.caption12m,
            color = contentColor,
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = contentColor,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatNoticePreview() {
    NapzakMarketTheme {
        ChatNotice(
            notice = "상대방이 채팅방을 나갔습니다.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
        )
    }
}
