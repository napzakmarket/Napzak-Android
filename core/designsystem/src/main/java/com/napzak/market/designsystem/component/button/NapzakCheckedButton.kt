package com.napzak.market.designsystem.component.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R
import com.napzak.market.designsystem.R.drawable.ic_checked_box
import com.napzak.market.designsystem.R.drawable.ic_unchecked_box
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.util.android.noRippleClickable


/**
 * 공통 체크 버튼 컴포넌트입니다.
 *
 * 체크 여부에 따라 서로 다른 아이콘이 표시되며 체크박스와 함께 텍스트가 수평으로 배치됩니다.
 * 납작 마켓 내 약관 동의, 항목 선택 등 사용자 입력이 필요한 다양한 영역에서 재사용 가능합니다.
 *
 * @param checked 체크 상태 (true일 경우 체크된 아이콘 표시)
 * @param text 체크박스 오른쪽에 표시될 텍스트
 * @param modifier Modifier를 통해 외부에서 레이아웃 및 스타일을 조절할 수 있습니다 (기본값: Modifier)
 * @param backgroundColor 컴포넌트 배경색 (기본값: NapzakMarketTheme.colors.white)
 * @param onCheckedChange 체크 상태가 변경될 때 호출되는 콜백
 * @param icon 텍스트 가장 오른쪽에 표시할 아이콘 (기본값: null)
 */

@Composable
fun NapzakCheckedButton(
    checked: Boolean,
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = NapzakMarketTheme.colors.white,
    onCheckedChange: (Boolean) -> Unit,
    icon: ImageVector? = null,
    onIconClick: (() -> Unit)? = null,
) {
    val checkIcon = if (checked) {
        ImageVector.vectorResource(ic_checked_box)
    } else {
        ImageVector.vectorResource(ic_unchecked_box)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = backgroundColor,

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = checkIcon,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(24.dp)
                    .clickable { onCheckedChange(!checked) },
                tint = Color.Unspecified,
            )
            Text(
                text = text,
                style = NapzakMarketTheme.typography.body14b,
                color = NapzakMarketTheme.colors.gray400,
                modifier = Modifier
                    .padding(vertical = 14.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = NapzakMarketTheme.colors.gray300,
                    modifier = Modifier
                        .size(16.dp)
                        .noRippleClickable(enabled = onIconClick != null) {
                            onIconClick?.invoke()
                        },
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun NapzakCheckItemPreview() {
    NapzakMarketTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .width(360.dp),
        ) {
            NapzakCheckedButton(
                checked = true,
                text = "약관 전체 동의",
                icon = ImageVector.vectorResource(id = R.drawable.ic_arrow_right),
                onCheckedChange = {},
                backgroundColor = NapzakMarketTheme.colors.white,
            )

            NapzakCheckedButton(
                checked = false,
                text = "배송비",
                icon = null,
                onCheckedChange = {},
                backgroundColor = NapzakMarketTheme.colors.gray50,
            )
        }
    }
}
