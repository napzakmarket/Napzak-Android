package com.napzak.market.main.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_purple_register_buy
import com.napzak.market.designsystem.R.drawable.ic_purple_register_sell
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.main.R.string.dialog_register_buy
import com.napzak.market.main.R.string.dialog_register_sell
import com.napzak.market.ui_util.noRippleClickable

@Composable
internal fun MainRegisterDialog(
    visibility: Boolean,
    onSellRegisterClick: () -> Unit,
    onBuyRegisterClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dialogShape = RoundedCornerShape(12.dp)
    val colorScheme = NapzakMarketTheme.colors

    AnimatedVisibility(
        visible = visibility,
        enter = slideInVertically { fullHeight -> fullHeight },
        exit = slideOutVertically { fullHeight -> fullHeight },
        modifier = modifier,
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
                .noRippleClickable(onDismissRequest)
                .padding(bottom = 10.dp),
        ) {
            Column(
                modifier = Modifier
                    .clip(dialogShape)
                    .shadow(elevation = 4.dp, clip = true)
                    .background(color = colorScheme.white)
                    .drawBehind {
                        drawLine(
                            color = colorScheme.gray200,
                            strokeWidth = Dp.Hairline.toPx(),
                            start = Offset(0f, size.height / 2),
                            end = Offset(size.width, size.height / 2),
                        )
                    }
            ) {
                RegisterNavigationButton(
                    image = ImageVector.vectorResource(ic_purple_register_sell),
                    label = stringResource(dialog_register_sell),
                    onClick = onSellRegisterClick,
                    modifier = Modifier.padding(5.dp),
                )

                RegisterNavigationButton(
                    image = ImageVector.vectorResource(ic_purple_register_buy),
                    label = stringResource(dialog_register_buy),
                    onClick = onBuyRegisterClick,
                    modifier = Modifier.padding(5.dp),
                )
            }
        }
    }
}

@Composable
private fun RegisterNavigationButton(
    image: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .noRippleClickable(onClick = onClick)
            .padding(horizontal = 46.dp, vertical = 10.dp),
    ) {
        Icon(
            imageVector = image,
            contentDescription = label,
            tint = Color.Unspecified,
        )
        Text(
            text = label,
            style = NapzakMarketTheme.typography.body14b,
            color = NapzakMarketTheme.colors.gray400,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterNavigationButtonGroupPreview() {
    NapzakMarketTheme {
        MainRegisterDialog(
            onSellRegisterClick = {},
            onBuyRegisterClick = {},
            onDismissRequest = {},
            visibility = true,
        )
    }
}
