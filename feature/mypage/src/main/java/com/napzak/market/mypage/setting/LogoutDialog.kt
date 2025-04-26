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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.napzak.market.designsystem.theme.NapzakMarketTheme

@Composable
internal fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        // 뒷배경 + 중앙 정렬
        Box(
            modifier = Modifier
        ) {
            // 다이얼로그 박스
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
                    // 제목
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "로그아웃 하시겠어요?",
                            style = NapzakMarketTheme.typography.title18b,
                            color = NapzakMarketTheme.colors.purple500,
                        )
                    }

                    // 가로 구분선
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(NapzakMarketTheme.colors.gray200),
                    )

                    // 버튼 영역
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
                                    text = "예",
                                    style = NapzakMarketTheme.typography.body14sb,
                                    color = NapzakMarketTheme.colors.gray300,
                                )
                            }
                        }

                        // 세로 구분선
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
                                    text = "아니요",
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