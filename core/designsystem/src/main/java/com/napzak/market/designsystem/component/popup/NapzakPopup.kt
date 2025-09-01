package com.napzak.market.designsystem.component.popup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.window.DialogProperties
import com.napzak.market.designsystem.R.drawable.ic_gray_arrow_right
import com.napzak.market.designsystem.R.drawable.ic_purple_change
import com.napzak.market.designsystem.theme.NapzakMarketTheme

@Composable
fun NapzakPopup(
    title: String,
    subTitle: String,
    icon: ImageVector,
    buttonColor: Color,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonIcon: ImageVector? = null,
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(horizontal = 30.dp)
                .background(
                    color = NapzakMarketTheme.colors.white,
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(horizontal = 26.dp, vertical = 24.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(34.dp),
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = title,
                style = NapzakMarketTheme.typography.body16b.copy(
                    color = NapzakMarketTheme.colors.black,
                ),
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = subTitle,
                style = NapzakMarketTheme.typography.caption12sb.copy(
                    color = NapzakMarketTheme.colors.gray200,
                    textAlign = TextAlign.Center,
                ),
            )

            Spacer(Modifier.height(12.dp))

            NapzakPopupButton(
                text = buttonText,
                buttonColor = buttonColor,
                icon = buttonIcon,
                onClick = onButtonClick,
                modifier = Modifier,
            )
        }
    }
}

@Composable
private fun NapzakPopupButton(
    text: String,
    buttonColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    Button(
        onClick = onClick,
        enabled = true,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = NapzakMarketTheme.colors.white,
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = text,
                style = NapzakMarketTheme.typography.body14b.copy(
                    color = NapzakMarketTheme.colors.white,
                ),
                modifier = Modifier.padding(vertical = 4.dp),
            )

            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = NapzakMarketTheme.colors.white,
                    modifier = Modifier.size(12.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun NapzakPopupPreview(modifier: Modifier = Modifier) {
    NapzakMarketTheme {
        NapzakPopup(
            title = "업데이트가 필요해요!",
            subTitle = "원활한 서비스 이용을 위해\n" + "최신 버전으로 업데이트해주세요.",
            icon = ImageVector.vectorResource(ic_purple_change),
            buttonColor = NapzakMarketTheme.colors.purple500,
            buttonText = "납작마켓 업데이트",
            buttonIcon = ImageVector.vectorResource(ic_gray_arrow_right),
            onButtonClick = {},
            modifier = modifier,
        )
    }
}
