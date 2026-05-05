package com.napzak.market.designsystem.component.popup

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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.napzak.market.designsystem.R.drawable.ic_gray_cancel
import com.napzak.market.designsystem.R.drawable.ic_phone_verification
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.ui_util.noRippleClickable

@Composable
fun NapzakModal(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    image: Int,
    buttonText: String,
    onDismissRequest: () -> Unit,
    onButtonClick: () -> Unit,
) {
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
                imageVector = ImageVector.vectorResource(ic_gray_cancel),
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
                    imageVector = ImageVector.vectorResource(image),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.padding(top = 6.dp),
                )

                Spacer(Modifier.height(18.dp))

                Text(
                    text = title,
                    style = NapzakMarketTheme.typography.body16b,
                    color = NapzakMarketTheme.colors.black,
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = content,
                    style = NapzakMarketTheme.typography.caption12sb,
                    color = NapzakMarketTheme.colors.gray200,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onButtonClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NapzakMarketTheme.colors.purple500,
                        contentColor = NapzakMarketTheme.colors.white,
                    ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = buttonText,
                            style = NapzakMarketTheme.typography.body14sb,
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
private fun NapzakModalPreview(modifier: Modifier = Modifier) {
    NapzakMarketTheme {
        NapzakModal(
            title = "본인확인이 필요해요",
            content = "안전한 거래를 위해 휴대폰 인증 후\n서비스를 이용할 수 있어요",
            image = ic_phone_verification,
            buttonText = "인증하기",
            onDismissRequest = {},
            onButtonClick = {},
            modifier = modifier,
        )
    }
}