package com.napzak.market.designsystem.component.toast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R
import com.napzak.market.designsystem.theme.NapzakMarketTheme

/**
 * 공통 스낵바 컴포넌트입니다.
 * 메시지와 함께 배경색, 텍스트 색상, 아이콘 등을 설정할 수 있으며,
 * 주로 사용자에게 피드백 메시지를 보여줄 때 사용됩니다.
 *
 * @param message 스낵바에 표시될 메시지 문자열
 * @param modifier Modifier를 통해 외부에서 스타일을 지정할 수 있습니다 (기본값: Modifier)
 * @param backgroundColor 스낵바의 배경 색상
 * @param textColor 메시지 텍스트 색상
 * @param textStyle 메시지 텍스트 스타일
 * @param icon 메시지 앞에 표시할 아이콘 (기본값: null, 없을 경우 아이콘 미표시)
 * @param contentPadding 스낵바의 내부 패딩 값
 * @param shape 스낵바의 모양을 결정하는 Shape (기본값: 14dp의 라운드 코너)
 */

@Composable
fun CommonSnackBar(
    message: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = NapzakMarketTheme.colors.transBlack,
    textColor: Color = NapzakMarketTheme.colors.white,
    textStyle: TextStyle = NapzakMarketTheme.typography.body14sb,
    icon: ImageVector? = null,
    contentPadding: PaddingValues = PaddingValues(vertical = 13.dp),
    shape: Shape = RoundedCornerShape(14.dp),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = shape,
            )
            .padding(contentPadding),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
            Text(
                text = message,
                style = textStyle,
                color = textColor,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CommonSnackBarPreview() {
    NapzakMarketTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .width(320.dp)
        ) {
            val snackBarModifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 13.dp)

            CommonSnackBar(
                message = stringResource(id = R.string.snackbar_reserve_message),
                icon = ImageVector.vectorResource(id = R.drawable.ic_white_checked),
                modifier = snackBarModifier,
            )

            CommonSnackBar(
                message = stringResource(id = R.string.snackbar_sell_message),
                icon = ImageVector.vectorResource(id = R.drawable.ic_white_checked),
                modifier = snackBarModifier,
            )

            CommonSnackBar(
                message = stringResource(id = R.string.snackbar_soldout_message),
                icon = ImageVector.vectorResource(id = R.drawable.ic_white_checked),
                modifier = snackBarModifier,
            )

            CommonSnackBar(
                message = stringResource(id = R.string.snackbar_deleted_message),
                icon = ImageVector.vectorResource(id = R.drawable.ic_gray_trash_bin),
                modifier = snackBarModifier,
            )
        }
    }
}