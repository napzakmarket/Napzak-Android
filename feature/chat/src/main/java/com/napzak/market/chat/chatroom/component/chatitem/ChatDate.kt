package com.napzak.market.chat.chatroom.component.chatitem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme

@Composable
internal fun ChatDate(
    date: String,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(27.dp)
    val color = NapzakMarketTheme.colors.gray200
    val borderStroke = BorderStroke(width = 1.dp, color = color)
    val innerPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
    val textStyle = NapzakMarketTheme.typography.caption12r

    Box(
        modifier = modifier
            .clip(shape)
            .border(
                border = borderStroke,
                shape = shape,
            )
            .padding(innerPadding),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = date,
            style = textStyle,
            color = color,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatDatePreview() {
    NapzakMarketTheme {
        ChatDate(date = "0000년 00월 00일")
    }
}
