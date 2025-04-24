package com.napzak.market.detail.component.divider

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme

@Composable
internal fun SectionDivider(
    modifier: Modifier = Modifier,
) {
    HorizontalDivider(
        thickness = 4.dp,
        color = NapzakMarketTheme.colors.gray50,
        modifier = modifier,
    )
}