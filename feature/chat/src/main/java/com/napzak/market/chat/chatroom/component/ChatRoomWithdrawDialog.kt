package com.napzak.market.chat.chatroom.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.napzak.market.chat.chatroom.state.ChatRoomPopupEvent
import com.napzak.market.designsystem.component.dialog.NapzakDialog
import com.napzak.market.designsystem.component.dialog.NapzakDialogDefault
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.chat.R.string.chat_room_block_dialog_cancel
import com.napzak.market.feature.chat.R.string.chat_room_block_dialog_confirm
import com.napzak.market.feature.chat.R.string.chat_room_block_dialog_content
import com.napzak.market.feature.chat.R.string.chat_room_block_dialog_title
import com.napzak.market.feature.chat.R.string.chat_room_withdraw_dialog_cancel
import com.napzak.market.feature.chat.R.string.chat_room_withdraw_dialog_confirm
import com.napzak.market.feature.chat.R.string.chat_room_withdraw_dialog_content
import com.napzak.market.feature.chat.R.string.chat_room_withdraw_dialog_title

@Composable
internal fun ChatRoomDialogSection(
    isWithdrawDialogVisible: Boolean,
    isBlockDialogVisible: Boolean,
    onWithdrawConfirm: () -> Unit,
    onBlockConfirm: () -> Unit,
    onDismissClick: (ChatRoomPopupEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isWithdrawDialogVisible) {
        NapzakDialog(
            title = stringResource(chat_room_withdraw_dialog_title),
            subTitle = stringResource(chat_room_withdraw_dialog_content),
            confirmText = stringResource(chat_room_withdraw_dialog_confirm),
            dismissText = stringResource(chat_room_withdraw_dialog_cancel),
            onConfirmClick = onWithdrawConfirm,
            onDismissClick = { onDismissClick(ChatRoomPopupEvent.DismissWithdrawDialog) },
            dialogColor = NapzakDialogDefault.color.copy(
                titleColor = NapzakMarketTheme.colors.black,
            ),
            modifier = modifier,
        )
    }

    if (isBlockDialogVisible) {
        NapzakDialog(
            title = stringResource(chat_room_block_dialog_title),
            subTitle = stringResource(chat_room_block_dialog_content),
            confirmText = stringResource(chat_room_block_dialog_confirm),
            dismissText = stringResource(chat_room_block_dialog_cancel),
            onConfirmClick = onBlockConfirm,
            onDismissClick = { onDismissClick(ChatRoomPopupEvent.DismissBlockDialog) },
            dialogColor = NapzakDialogDefault.color.copy(
                titleColor = NapzakMarketTheme.colors.black,
            ),
            modifier = modifier,
        )
    }
}