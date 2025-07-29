package com.napzak.market.home.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_footer_instagram
import com.napzak.market.designsystem.R.drawable.ic_footer_mail
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.home.R.string.home_footer_email
import com.napzak.market.feature.home.R.string.home_footer_instagram
import com.napzak.market.feature.home.R.string.home_footer_leader
import com.napzak.market.feature.home.R.string.home_footer_leader_name
import com.napzak.market.feature.home.R.string.home_footer_license
import com.napzak.market.feature.home.R.string.home_footer_privacy_policy
import com.napzak.market.feature.home.R.string.home_footer_service_usage_terms
import com.napzak.market.feature.home.R.string.home_footer_title
import com.napzak.market.ui_util.noRippleClickable
import com.napzak.market.ui_util.openUrl

@Composable
internal fun HomeFooter(
    termsLink: String,
    privacyPolicyLink: String,
    modifier: Modifier = Modifier,
) {
    val typography = NapzakMarketTheme.typography
    val colors = NapzakMarketTheme.colors
    val context = LocalContext.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CommonText(
            id = home_footer_title,
            style = typography.caption12sb,
            color = colors.gray500,
        )

        Spacer(modifier = Modifier.height(8.dp))


        IconText(
            stringId = home_footer_email,
            drawableId = ic_footer_mail,
            textStyle = typography.caption10r,
            color = colors.gray300,
        )

        Spacer(modifier = Modifier.height(4.dp))

        IconText(
            stringId = home_footer_instagram,
            drawableId = ic_footer_instagram,
            textStyle = typography.caption10r,
            color = colors.gray300,
        )


        Spacer(modifier = Modifier.height(20.dp))

        DividerTexts(
            leftStringId = home_footer_service_usage_terms,
            rightStringId = home_footer_privacy_policy,
            textStyle = typography.caption10sb,
            color = colors.gray500,
            horizontalSpace = 8.dp,
            onLeftStringClick = { context.openUrl(termsLink) },
            onRightStringClick = { context.openUrl(privacyPolicyLink) },
        )

        Spacer(modifier = Modifier.height(20.dp))

        CommonText(
            id = home_footer_license,
            style = typography.caption10r,
            color = colors.gray300,
        )

        Spacer(modifier = Modifier.height(23.dp))

        DividerTexts(
            leftStringId = home_footer_leader,
            rightStringId = home_footer_leader_name,
            textStyle = typography.caption10sb,
            color = colors.gray300,
            horizontalSpace = 4.dp
        )
    }
}

@Composable
private fun DividerTexts(
    @StringRes leftStringId: Int,
    @StringRes rightStringId: Int,
    textStyle: TextStyle,
    color: Color,
    horizontalSpace: Dp,
    modifier: Modifier = Modifier,
    onLeftStringClick: () -> Unit = {},
    onRightStringClick: () -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(horizontalSpace)
    ) {
        CommonText(
            id = leftStringId,
            style = textStyle,
            color = color,
            modifier = Modifier.noRippleClickable(onClick = onLeftStringClick)
        )

        VerticalDivider(
            modifier = Modifier.height(7.dp),
            color = NapzakMarketTheme.colors.gray200,
        )

        CommonText(
            id = rightStringId,
            style = textStyle,
            color = color,
            modifier = Modifier.noRippleClickable(onClick = onRightStringClick)
        )
    }
}

@Composable
private fun IconText(
    @StringRes stringId: Int,
    @DrawableRes drawableId: Int,
    textStyle: TextStyle,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Row(
        modifier.noRippleClickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(drawableId),
            contentDescription = null,
            tint = color
        )

        CommonText(
            id = stringId,
            style = textStyle,
            color = color,
        )
    }
}

@Composable
private fun CommonText(
    @StringRes id: Int,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id),
        style = style,
        color = color,
        maxLines = 1,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeFooterPreview() {
    NapzakMarketTheme {
        HomeFooter(
            termsLink = "",
            privacyPolicyLink = "",
        )
    }
}