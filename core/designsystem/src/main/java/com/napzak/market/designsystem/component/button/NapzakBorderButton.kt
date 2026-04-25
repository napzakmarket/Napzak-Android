package com.napzak.market.designsystem.component.button

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme

/**
 * border가 있는 버튼 컴포넌트입니다.
 * 활성/비활성 상태** 두 가지 스타일을 기본 제공하며
 * 버튼 내부에 **텍스트만** 사용할 수 있습니다.
 * 납작 primary color가 적용되는 모든 버튼에서 사용 가능합니다.
 *
 * @param text 버튼에 표시될 텍스트
 * @param onClick 버튼 클릭 시 실행할 로직
 * @param enabled 버튼 활성화 여부 (기본값: true)
 * @param modifier 외부에서 버튼 크기나 위치 등을 조절할 수 있는 Modifier (기본값: Modifier)
 */

@Composable
fun NapzakBorderButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val containerColor = NapzakMarketTheme.colors.white
    val contentColor =
        if (enabled) NapzakMarketTheme.colors.purple500 else NapzakMarketTheme.colors.gray100

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = contentColor,
                shape = RoundedCornerShape(14.dp),
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor,
            disabledContentColor = contentColor,
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
                modifier = Modifier
                    .padding(vertical = 8.dp),
            )
        }
    }
}

@Preview
@Composable
private fun NapzakBorderButtonEnabledPreview() {
    NapzakMarketTheme {
        NapzakBorderButton(
            text = "인증번호 확인하기",
            onClick = {},
            enabled = true,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun NapzakBorderButtonPreview() {
    NapzakMarketTheme {
        NapzakBorderButton(
            text = "인증번호 확인하기",
            onClick = {},
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}