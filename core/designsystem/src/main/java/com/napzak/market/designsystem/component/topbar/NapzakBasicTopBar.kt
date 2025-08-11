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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.ui_util.ShadowDirection
import com.napzak.market.ui_util.napzakGradientShadow
import com.napzak.market.ui_util.noRippleClickable

@Composable
fun NapzakBasicTopBar(
    isShadowed: Boolean,
    title: String?,
    titleAlign: TextAlign?,
    navigators: List<NapzakTopBarAction>?,
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
                        height = 4.dp,
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
        navigators?.forEach { navigation ->
            ActionButton(
                iconRes = navigation.iconRes,
                color = color.iconColor,
                onClick = navigation.onClick,
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        Spacer(modifier = Modifier.width(2.dp))

        if (title != null) {
            Text(
                text = title,
                style = NapzakMarketTheme.typography.body16b,
                color = color.contentColor,
                textAlign = titleAlign,
                modifier = Modifier.weight(1f),
            )
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        actions?.forEach { action ->
            Spacer(modifier = Modifier.width(4.dp))
            ActionButton(
                iconRes = action.iconRes,
                color = color.iconColor,
                onClick = action.onClick,
            )
        }
    }
}

@Composable
private fun ActionButton(
    @DrawableRes iconRes: Int?,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
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

data class NapzakTopBarAction(
    @DrawableRes val iconRes: Int?,
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
