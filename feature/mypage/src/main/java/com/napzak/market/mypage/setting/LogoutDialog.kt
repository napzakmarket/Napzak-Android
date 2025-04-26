package com.napzak.market.mypage.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.mypage.R.string.settings_logout_dialog_title
import com.napzak.market.feature.mypage.R.string.settings_logout_dialog_confirm_button
import com.napzak.market.feature.mypage.R.string.settings_logout_dialog_cancel_button

@Composable
internal fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = modifier
        ) {
            Box(
                modifier = Modifier
                    .width(285.dp)
                    .height(150.dp)
                    .background(
                        color = NapzakMarketTheme.colors.white,
                        shape = RoundedCornerShape(14.dp),
                    )
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(id = settings_logout_dialog_title),
                            style = NapzakMarketTheme.typography.title18b,
                            color = NapzakMarketTheme.colors.purple500,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(NapzakMarketTheme.colors.gray200),
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            Button(
                                onClick = onConfirm,
                                modifier = Modifier.fillMaxSize(),
                                colors = ButtonDefaults.buttonColors(containerColor = NapzakMarketTheme.colors.white),
                            ) {
                                Text(
                                    text = stringResource(id = settings_logout_dialog_confirm_button),
                                    style = NapzakMarketTheme.typography.body14sb,
                                    color = NapzakMarketTheme.colors.gray300,
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .fillMaxHeight()
                                .background(NapzakMarketTheme.colors.gray200),
                        )

                        Box(modifier = Modifier.weight(1f)) {
                            Button(
                                onClick = onDismiss,
                                modifier = Modifier.fillMaxSize(),
                                colors = ButtonDefaults.buttonColors(containerColor = NapzakMarketTheme.colors.white),
                            ) {
                                Text(
                                    text = stringResource(id = settings_logout_dialog_cancel_button),
                                    style = NapzakMarketTheme.typography.body14sb,
                                    color = NapzakMarketTheme.colors.gray300,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun LogoutDialogPreview() {
    NapzakMarketTheme {
        LogoutDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}