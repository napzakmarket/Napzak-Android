package com.napzak.market.designsystem.component.bottomsheet

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R
import com.napzak.market.designsystem.R.drawable.ic_down_chevron
import com.napzak.market.designsystem.R.drawable.ic_up_chevron
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.util.android.noRippleClickable

/**
 * bottomSheet 안에 들어가는 메뉴 아이템
 *
 * @param menuIcon 아이콘 이미지
 * @param menuName 메뉴 이름
 * @param onItemClick 아이템 클릭 시 실행됨
 * @param textStyle 텍스트 스타일
 * @param textColor 텍스트 컬러
 * @param isToggleOption 토글 버튼 필요 여부
 * @param isToggleOpen 토글 버튼의 열림 여부
 */

@Composable
fun BottomSheetMenuItem(
    menuIcon: ImageVector,
    menuName: String,
    onItemClick: () -> Unit,
    textStyle: TextStyle = NapzakMarketTheme.typography.body14b,
    textColor: Color = NapzakMarketTheme.colors.gray500,
    isToggleOption: Boolean = false,
    isToggleOpen: Boolean = false,
) {
    val chevronIcon = if (isToggleOpen) ImageVector.vectorResource(ic_up_chevron)
    else ImageVector.vectorResource(ic_down_chevron)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable(onItemClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = menuIcon,
            contentDescription = null,
            tint = Color.Unspecified,
        )

        Spacer(Modifier.width(6.dp))

        Text(
            text = menuName,
            style = textStyle,
            color = textColor,
        )

        Spacer(Modifier.weight(1f))

        if (isToggleOption) {
            Icon(
                imageVector = chevronIcon,
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }
    }
}

@Preview
@Composable
private fun BottomSheetMenuItemPreview() {
    NapzakMarketTheme {
        BottomSheetMenuItem(
            menuIcon = ImageVector.vectorResource(R.drawable.ic_delete_24),
            menuName = "삭제하기",
            onItemClick = { },
        )
    }
}