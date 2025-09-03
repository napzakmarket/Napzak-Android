package com.napzak.market.login.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.napzak.market.designsystem.R.drawable.ic_square_red_warning
import com.napzak.market.designsystem.component.popup.NapzakPopup
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.login.R.string.reported_popup_subtitle
import com.napzak.market.feature.login.R.string.reported_popup_title
import com.napzak.market.feature.login.R.string.reported_popup_confirm_button

@Composable
fun UserReportedPopup(
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NapzakPopup(
        title = stringResource(reported_popup_title),
        subTitle = stringResource(reported_popup_subtitle),
        icon = ImageVector.vectorResource(ic_square_red_warning),
        buttonColor = NapzakMarketTheme.colors.red,
        buttonText = stringResource(reported_popup_confirm_button),
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
