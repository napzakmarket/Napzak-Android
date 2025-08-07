package com.napzak.market.designsystem.component.topbar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_chevron_left_24
import com.napzak.market.designsystem.R.string.top_bar_navigate_up
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.ui_util.ShadowDirection
import com.napzak.market.ui_util.napzakGradientShadow
import com.napzak.market.ui_util.noRippleClickable

@Composable
internal fun NapzakBasicTopBar(
    isShadowed: Boolean,
    title: String?,
    titleAlign: TextAlign?,
    onNavigateUp: (() -> Unit)?,
    actions: List<NapzakTopBarAction>?,
    paddingValues: PaddingValues,
    color: NapzakTopBarColor,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(color = color.containerColor)
            .then(
                if (isShadowed) {
                    Modifier.napzakGradientShadow(
                        height = 2.dp,
                        startColor = NapzakMarketTheme.colors.shadowBlack,
                        endColor = NapzakMarketTheme.colors.transWhite,
                        direction = ShadowDirection.Bottom,
                    )
                } else {
                    Modifier
                }
            )
            .padding(paddingValues)
    ) {
        if (onNavigateUp != null) {
            Icon(
                imageVector = ImageVector.vectorResource(ic_chevron_left_24),
                contentDescription = stringResource(top_bar_navigate_up),
                tint = color.iconColor,
                modifier = Modifier
                    .semantics { role = Role.Button }
                    .noRippleClickable(onClick = onNavigateUp),
            )
        }

        Spacer(modifier = Modifier.width(2.dp))

        if (title != null) {
            Text(
                text = title,
                style = NapzakMarketTheme.typography.body16b,
                color = color.contentColor,
                textAlign = titleAlign,
                modifier = Modifier.weight(1f)
            )
        }

        actions?.forEach { action ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .semantics { role = Role.Button }
                    .size(24.dp)
                    .noRippleClickable(onClick = action.onClick),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(action.iconRes),
                    contentDescription = null,
                    tint = color.iconColor,
                )
            }
        }
    }
}

data class NapzakTopBarAction(
    @DrawableRes val iconRes: Int,
    val onClick: () -> Unit,
)

data class NapzakTopBarColor(
    val iconColor: Color,
    val contentColor: Color,
    val containerColor: Color,
)

object NapzakTopBarDefault {
    internal val topBarInnerPadding =
        PaddingValues(start = 13.dp, end = 13.dp, top = 34.dp, bottom = 18.dp)

    @Composable
    internal fun topBarColor(): NapzakTopBarColor = with(NapzakMarketTheme.colors) {
        NapzakTopBarColor(
            iconColor = gray200,
            contentColor = gray400,
            containerColor = white,
        )
    }
}
