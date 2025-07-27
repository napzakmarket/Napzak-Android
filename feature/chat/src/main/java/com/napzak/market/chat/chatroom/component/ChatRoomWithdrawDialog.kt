package com.napzak.market.chat.chatroom.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.chat.R.string.chat_room_withdraw_dialog_cancel
import com.napzak.market.feature.chat.R.string.chat_room_withdraw_dialog_confirm
import com.napzak.market.feature.chat.R.string.chat_room_withdraw_dialog_content
import com.napzak.market.feature.chat.R.string.chat_room_withdraw_dialog_title
import com.napzak.market.ui_util.ScreenPreview

@Composable
internal fun NapzakWithdrawDialog(
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = NapzakMarketTheme.colors
    val typography = NapzakMarketTheme.typography

    Dialog(
        onDismissRequest = onDismissClick,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(horizontal = 30.dp)
                .background(
                    color = NapzakMarketTheme.colors.white,
                    shape = RoundedCornerShape(14.dp),
                )
                .padding(26.dp)
        ) {
            Text(
                text = stringResource(chat_room_withdraw_dialog_title),
                style = typography.body16b.copy(
                    color = colorScheme.black,
                ),
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(chat_room_withdraw_dialog_content),
                style = NapzakMarketTheme.typography.caption12sb.copy(
                    color = colorScheme.gray200,
                    textAlign = TextAlign.Center,
                ),
            )
            Spacer(Modifier.height(15.dp))

            Row {
                DialogButton(
                    text = stringResource(chat_room_withdraw_dialog_confirm),
                    textColor = colorScheme.white,
                    backgroundColor = colorScheme.purple500,
                    onClick = onConfirmClick,
                    modifier = Modifier.weight(1f),
                )
                Spacer(Modifier.width(10.dp))
                DialogButton(
                    text = stringResource(chat_room_withdraw_dialog_cancel),
                    textColor = colorScheme.gray400,
                    backgroundColor = colorScheme.gray50,
                    onClick = onDismissClick,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun DialogButton(
    text: String,
    textColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp),
            )
            .clickable(onClick = onClick),
    ) {
        Text(
            text = text,
            style = NapzakMarketTheme.typography.body14sb.copy(
                color = textColor,
            ),
            modifier = Modifier.padding(vertical = 10.dp),
        )
    }
}

@ScreenPreview()
@Composable
private fun NapzakWithdrawDialogPreview() {
    NapzakMarketTheme {
        NapzakWithdrawDialog(
            onConfirmClick = {},
            onDismissClick = {},
        )
    }
}