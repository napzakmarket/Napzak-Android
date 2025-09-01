package com.napzak.market.login.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.napzak.market.designsystem.R.drawable.ic_square_red_warning
import com.napzak.market.designsystem.component.popup.NapzakPopup
import com.napzak.market.designsystem.theme.NapzakMarketTheme

@Composable
fun UserReportedPopup(
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NapzakPopup(
        title = "접근이 불가합니다.",
        subTitle = "해당 계정은 정책 위반으로 인해\n앱 서비스 접근이 불가합니다.",
        icon = ImageVector.vectorResource(ic_square_red_warning),
        buttonColor = NapzakMarketTheme.colors.red,
        buttonText = "확인",
        buttonIcon = null,
        onButtonClick = onButtonClick,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun UserReportedPopupPreview(modifier: Modifier = Modifier) {
    NapzakMarketTheme {
        UserReportedPopup(
            onButtonClick = {},
            modifier = modifier,
        )
    }
}
