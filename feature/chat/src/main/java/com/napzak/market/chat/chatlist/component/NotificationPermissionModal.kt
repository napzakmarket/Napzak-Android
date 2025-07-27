package com.napzak.market.chat.chatlist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.napzak.market.designsystem.R.drawable.ic_close_24
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.chat.R.drawable.img_notification_permission
import com.napzak.market.feature.chat.R.string.permission_modal_button_name
import com.napzak.market.ui_util.noRippleClickable

@Composable
fun NotificationPermissionModal(
    title: String,
    content: String,
    onDismissRequest: () -> Unit,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = NapzakMarketTheme.colors.purple500
    val contentColor = NapzakMarketTheme.colors.white

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Box(
            modifier = modifier
                .width(284.dp)
                .background(NapzakMarketTheme.colors.white, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.TopEnd,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(ic_close_24),
                contentDescription = null,
                tint = NapzakMarketTheme.colors.gray200,
                modifier = Modifier
                    .noRippleClickable(onDismissRequest)
                    .padding(10.dp),
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 26.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(img_notification_permission),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = modifier.padding(top = 6.dp)
                )

                Spacer(Modifier.height(18.dp))

                Text(
                    text = title,
                    style = NapzakMarketTheme.typography.body16b.copy(
                        NapzakMarketTheme.colors.black
                    )
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = content,
                    style = NapzakMarketTheme.typography.caption12sb.copy(
                        NapzakMarketTheme.colors.gray200
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onButtonClick,
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = backgroundColor,
                        contentColor = contentColor,
                    ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = stringResource(permission_modal_button_name),
                            style = NapzakMarketTheme.typography.body14sb.copy(contentColor),
                            modifier = Modifier.padding(vertical = 4.dp),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun NotificationPermissionModalPreview(modifier: Modifier = Modifier) {
    NapzakMarketTheme {
        NotificationPermissionModal(
            title = "앱과 기기 알림이 모두 꺼져있어요!",
            content = "중요한 거래 소식을 놓치지 않도록\n설정을 변경해 주세요.",
            onDismissRequest = {},
            onButtonClick = {},
            modifier = modifier,
        )
    }
}
