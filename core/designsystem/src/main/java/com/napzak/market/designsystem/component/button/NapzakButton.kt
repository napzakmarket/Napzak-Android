package com.napzak.market.designsystem.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R
import com.napzak.market.designsystem.theme.NapzakMarketTheme

/**
 * 공통 기본 버튼 컴포넌트입니다.
 * 활성/비활성 상태** 두 가지 스타일을 기본 제공하며
 * 버튼 내부에 **텍스트만** 또는 **텍스트 + 아이콘** 조합으로 사용할 수 있습니다.
 * 납작 primary color가 적용되는 모든 버튼에서 사용 가능합니다.
 *
 * @param text 버튼에 표시될 텍스트
 * @param onClick 버튼 클릭 시 실행할 로직
 * @param enabled 버튼 활성화 여부 (기본값: true)
 * @param icon 텍스트 오른쪽에 표시할 아이콘 (Painter 형식, 기본값: null)
 * @param modifier 외부에서 버튼 크기나 위치 등을 조절할 수 있는 Modifier (기본값: Modifier)
 */

@Composable
fun NapzakButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: Painter? = null,
) {
    val backgroundColor =
        if (enabled) NapzakMarketTheme.colors.purple500 else NapzakMarketTheme.colors.gray100
    val contentColor = NapzakMarketTheme.colors.white

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
        ),
        shape = RoundedCornerShape(14.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = text,
                style = NapzakMarketTheme.typography.body14b,
                color = contentColor,
            )
            if (icon != null) {
                Image(
                    painter = icon,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(contentColor),
                    modifier = Modifier.size(12.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NapzakButtonPreview() {
    NapzakMarketTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(20.dp)
                .width(360.dp)
        ) {
            NapzakButton(
                text = "다음으로",
                onClick = {},
                icon = painterResource(id = R.drawable.ic_arrow_right),
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth(),
            )

            NapzakButton(
                text = "다음으로",
                onClick = {},
                icon = painterResource(id = R.drawable.ic_arrow_right),
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}
