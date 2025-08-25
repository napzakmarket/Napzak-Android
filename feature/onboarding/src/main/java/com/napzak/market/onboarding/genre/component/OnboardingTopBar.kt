package com.napzak.market.onboarding.genre.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_gray_arrow_left
import com.napzak.market.designsystem.component.topbar.NapzakBasicTopBar
import com.napzak.market.designsystem.component.topbar.NapzakTopBarAction
import com.napzak.market.designsystem.component.topbar.NapzakTopBarColor

@Composable
internal fun OnboardingTopBar(
    onBackClick: () -> Unit,
    @DrawableRes indicatorIcon: Int,
    modifier: Modifier = Modifier,
) {
    val navigators = listOf(NapzakTopBarAction(ic_gray_arrow_left, onBackClick))
    val actions = listOf(NapzakTopBarAction(indicatorIcon, onClick = {}))
    val topBarColor = NapzakTopBarColor(
        iconColor = Color.Unspecified,
        contentColor = Color.Unspecified,
        containerColor = Color.Unspecified,
    )

    NapzakBasicTopBar(
        navigators = navigators,
        actions = actions,
        isShadowed = false,
        color = topBarColor,
        paddingValues = PaddingValues(0.dp),
        modifier = modifier,
        title = null,
        titleAlign = null,
    )
}

