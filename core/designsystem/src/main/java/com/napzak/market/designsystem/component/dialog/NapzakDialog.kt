package com.napzak.market.designsystem.component.dialog

import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.napzak.market.designsystem.R.string.dialog_text_confirm
import com.napzak.market.designsystem.R.string.dialog_text_dismiss
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.ui_util.throttledNoRippleClickable

/**
 * 납작마켓 다이얼로그 컴포넌트입니다.
 *
 * onDismissRequest와 onDismissClick의 이벤트가 "같은 경우" 이 컴포넌트를 사용합니다.
 */
@Composable
fun NapzakDialog(
    title: String,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier,
    subTitle: String? = null,
    confirmText: String = stringResource(dialog_text_confirm),
    dismissText: String = stringResource(dialog_text_dismiss),
    dialogColor: NapzakDialogColor = NapzakDialogDefault.color,
) {
    NapzakDialog(
        title = title,
        onConfirmClick = onConfirmClick,
        onDismissClick = onDismissClick,
        onDismissRequest = onDismissClick,
        subTitle = subTitle,
        confirmText = confirmText,
        dismissText = dismissText,
        dialogColor = dialogColor,
        modifier = modifier,
    )
}

/**
 * 납작마켓 다이얼로그 컴포넌트입니다.
 *
 * onDismissRequest와 onDismissClick의 이벤트가 "다른 경우" 이 컴포넌트를 사용합니다.
 */
@Composable
fun NapzakDialog(
    title: String,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    subTitle: String? = null,
    confirmText: String = stringResource(dialog_text_confirm),
    dismissText: String = stringResource(dialog_text_dismiss),
    dialogColor: NapzakDialogColor = NapzakDialogDefault.color,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(horizontal = 30.dp)
                .background(
                    color = dialogColor.containerColor,
                    shape = RoundedCornerShape(14.dp),
                )
                .padding(26.dp),
        ) {
            if (subTitle == null) Spacer(Modifier.height(20.dp))
            else Spacer(Modifier.height(10.dp))

            Text(
                text = title,
                style = NapzakMarketTheme.typography.body16b.copy(
                    color = dialogColor.titleColor,
                ),
            )

            if (subTitle != null) {
                Text(
                    text = subTitle,
                    style = NapzakMarketTheme.typography.caption12sb.copy(
                        color = dialogColor.subTitleColor,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
                )
            } else {
                Spacer(Modifier.height(35.dp))
            }
            DialogButtons(
                confirmText = confirmText,
                dismissText = dismissText,
                dialogColor = dialogColor,
                onConfirmClick = onConfirmClick,
                onDismissClick = onDismissClick,
            )
        }
    }
}

@Composable
private fun DialogButtons(
    confirmText: String,
    dismissText: String,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
    dialogColor: NapzakDialogColor,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
    ) {
        DialogButton(
            text = confirmText,
            textColor = dialogColor.confirmTextColor,
            backgroundColor = dialogColor.confirmColor,
            onClick = onConfirmClick,
            modifier = Modifier.weight(1f),
        )

        Spacer(Modifier.width(10.dp))

        DialogButton(
            text = dismissText,
            textColor = dialogColor.dismissTextColor,
            backgroundColor = dialogColor.dismissColor,
            onClick = onDismissClick,
            modifier = Modifier.weight(1f),
        )
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
            .throttledNoRippleClickable(onClick = onClick),
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


@Preview(showBackground = true)
@Composable
private fun DeleteDialogPreview() {
    NapzakMarketTheme {
        NapzakDialog(
            title = "상품을 정말 삭제할까요?",
            subTitle = "한번 삭제한 상품은 다시 되돌릴 수 없어요.",
            dialogColor = NapzakDialogDefault.color.copy(
                titleColor = NapzakMarketTheme.colors.red
            ),
            onConfirmClick = {},
            onDismissClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LogOutDialogPreview() {
    NapzakMarketTheme {
        NapzakDialog(
            title = "로그아웃 하시겠어요?",
            onConfirmClick = {},
            onDismissClick = {},
        )
    }
}
