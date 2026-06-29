package com.napzak.market.detail.component.topbar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_gray_arrow_left
import com.napzak.market.designsystem.R.drawable.ic_kebab
import com.napzak.market.designsystem.R.drawable.ic_share
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.detail.R.drawable.img_tooltip_product_state
import com.napzak.market.ui_util.ShadowDirection
import com.napzak.market.ui_util.napzakGradientShadow
import com.napzak.market.ui_util.noRippleClickable
import com.skydoves.balloon.compose.BalloonState
import com.skydoves.balloon.compose.balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder
import com.skydoves.balloon.compose.rememberBalloonState
import com.skydoves.balloon.compose.setBackgroundColor

@Composable
internal fun DetailTopBar(
    showToolTip: Boolean,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onOptionClick: () -> Unit,
    onTooltipDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val balloonState = productStatusBalloonState(
        showToolTip = showToolTip,
        onBalloonDismiss = onTooltipDismiss,
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .background(color = NapzakMarketTheme.colors.white)
            .napzakGradientShadow(
                height = 4.dp,
                startColor = NapzakMarketTheme.colors.shadowBlack,
                endColor = NapzakMarketTheme.colors.transWhite,
                direction = ShadowDirection.Bottom,
            )
            .padding(start = 13.dp, end = 13.dp, top = 34.dp, bottom = 18.dp)
    ) {
        ActionButton(
            iconRes = ic_gray_arrow_left,
            onClick = onBackClick,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ActionButton(
                iconRes = ic_share,
                onClick = onShareClick,
            )

            ActionButton(
                iconRes = ic_kebab,
                onClick = onOptionClick,
                modifier = Modifier.then(
                    balloonState?.let { state ->
                        Modifier.balloon(state) {
                            Image(
                                painter = painterResource(img_tooltip_product_state),
                                contentDescription = null,
                                modifier = Modifier.size(width = 264.dp, height = 68.dp)
                            )
                        }
                    } ?: Modifier
                )
            )
        }
    }
}

@Composable
private fun ActionButton(
    @DrawableRes iconRes: Int?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = NapzakMarketTheme.colors.gray200,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .semantics { role = Role.Button }
            .size(24.dp)
            .noRippleClickable(onClick = onClick),
    ) {
        if (iconRes != null) {
            Icon(
                imageVector = ImageVector.vectorResource(iconRes),
                contentDescription = null,
                tint = color,
            )
        }
    }
}

@Composable
private fun productStatusBalloonState(
    showToolTip: Boolean,
    onBalloonDismiss: () -> Unit,
): BalloonState? {
    return when {
        showToolTip -> {
            val builder = rememberBalloonBuilder {
                setBackgroundColor(Color.Transparent)
                setArrowSize(0)
                setMarginRight(6)
                setOnBalloonDismissListener { onBalloonDismiss() }
            }
            val balloonState = rememberBalloonState(builder)

            LaunchedEffect(Unit) {
                balloonState.showAlignBottom()
            }

            balloonState
        }

        else -> null
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DetailTopBarPreview() {
    NapzakMarketTheme {
        Column(
            Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            DetailTopBar(
                onBackClick = {},
                onShareClick = {},
                onOptionClick = {},
                onTooltipDismiss = {},
                showToolTip = true,
            )
        }
    }
}
