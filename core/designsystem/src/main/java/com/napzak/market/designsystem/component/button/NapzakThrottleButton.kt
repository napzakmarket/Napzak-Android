package com.napzak.market.designsystem.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R
import com.napzak.market.designsystem.theme.NapzakMarketTheme

/**
 * 연속적인 이벤트 실행을 방지하기 위해 스로틀링이 적용된 공통 기본 버튼 컴포넌트입니다.
 *
 * @param text 버튼에 표시될 텍스트
 * @param onClick 버튼 클릭 시 실행할 로직
 * @param enabled 버튼 활성화 여부 (기본값: true)
 * @param icon 텍스트 오른쪽에 표시할 아이콘 (기본값: null)
 * @param modifier 외부에서 버튼 크기나 위치 등을 조절할 수 있는 Modifier (기본값: Modifier)
 */

@Composable
fun NapzakThrottleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    throttleTime: Long = 2000L
) {
    var lastClickTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    fun throttle() {
        val now = System.currentTimeMillis()
        if (now - lastClickTime > throttleTime) {
            lastClickTime = now
            onClick()
        }
    }

    NapzakButton(
        text = text,
        onClick = ::throttle,
        enabled = enabled,
        icon = icon,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun NapzakThrottleButtonPreview() {
    NapzakMarketTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .width(360.dp)
        ) {
            NapzakThrottleButton(
                text = "다음으로",
                onClick = {},
                icon = ImageVector.vectorResource(id = R.drawable.ic_gray_arrow_right),
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth(),
            )

            NapzakThrottleButton(
                text = "다음으로",
                onClick = {},
                icon = ImageVector.vectorResource(id = R.drawable.ic_gray_arrow_right),
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}
