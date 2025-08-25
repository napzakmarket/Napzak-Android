package com.napzak.market.chat.chatroom.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.napzak.market.designsystem.component.dialog.NapzakDialog
import com.napzak.market.designsystem.component.dialog.NapzakDialogDefault
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
    NapzakDialog(
        title = stringResource(chat_room_withdraw_dialog_title),
        subTitle = stringResource(chat_room_withdraw_dialog_content),
        confirmText = stringResource(chat_room_withdraw_dialog_confirm),
        dismissText = stringResource(chat_room_withdraw_dialog_cancel),
        onConfirmClick = onConfirmClick,
        onDismissClick = onDismissClick,
        dialogColor = NapzakDialogDefault.color.copy(
            titleColor = NapzakMarketTheme.colors.black,
        ),
        modifier = modifier,
    )
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