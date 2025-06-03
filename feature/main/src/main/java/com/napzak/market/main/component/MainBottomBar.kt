package com.napzak.market.main.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.main.MainTab
import com.napzak.market.ui_util.noRippleClickable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private const val DEFAULT_ANIMATION_VALUE = 0f
private const val TARGET_ANIMATION_VALUE = 45f
private const val ANIMATION_DURATION_IN_MILLIS = 200

@Composable
internal fun MainBottomBar(
    isVisible: Boolean,
    tabs: ImmutableList<MainTab>,
    currentTab: MainTab?,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideIn { IntOffset(0, 0) },
        exit = fadeOut() + slideOut { IntOffset(0, 0) },
        modifier = modifier,
    ) {
        Surface(
            color = White,
            border = BorderStroke(
                width = 1.dp,
                color = NapzakMarketTheme.colors.gray50,
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 5.dp)
                    .defaultMinSize(minHeight = 88.dp)  //피그마상 바텀바 높이로 고정
                    .padding(top = 15.dp, bottom = 28.dp)
                    .selectableGroup(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                tabs.forEach { tab ->
                    val isSelected = tab == currentTab
                    val iconColor =
                        if (isSelected) NapzakMarketTheme.colors.black
                        else NapzakMarketTheme.colors.gray200
                    val textColor =
                        if (isSelected) NapzakMarketTheme.colors.purple500
                        else NapzakMarketTheme.colors.gray200

                    MainNavigationBarItem(
                        tab = tab,
                        onClick = { onTabSelected(tab) },
                        textColor = textColor,
                        modifier = Modifier
                            .weight(1f),
                        iconContent = {
                            if (tab == MainTab.REGISTER) {
                                RegisterIcon(
                                    imageVector = ImageVector.vectorResource(tab.iconRes),
                                    contentDescription = stringResource(tab.title),
                                    isSelected = isSelected,
                                    iconColor = iconColor,
                                )
                            } else {
                                Icon(
                                    imageVector = ImageVector.vectorResource(tab.iconRes),
                                    contentDescription = stringResource(tab.title),
                                    tint = iconColor,
                                )
                            }
                        },
                    )
                }

            }
        }
    }
}

@Composable
private fun MainNavigationBarItem(
    tab: MainTab,
    textColor: Color,
    onClick: () -> Unit,
    iconContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .semantics(mergeDescendants = true) { role = Role.Tab }
            .noRippleClickable(onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        iconContent()
        Spacer(Modifier.height(5.dp))
        Text(
            text = stringResource(tab.title),
            style = NapzakMarketTheme.typography.caption12sb,
            color = textColor,
        )
    }
}

@Composable
private fun RegisterIcon(
    imageVector: ImageVector,
    contentDescription: String,
    isSelected: Boolean,
    iconColor: Color,
) {
    var hasLaunchedOnce by remember { mutableStateOf(false) }
    val rotation = remember { Animatable(DEFAULT_ANIMATION_VALUE) }

    LaunchedEffect(isSelected) {
        if (hasLaunchedOnce) {
            val targetValue = if (isSelected) rotation.value + TARGET_ANIMATION_VALUE
            else DEFAULT_ANIMATION_VALUE

            rotation.animateTo(
                targetValue = targetValue,
                animationSpec = tween(
                    durationMillis = ANIMATION_DURATION_IN_MILLIS,
                ),
            )
        } else {
            hasLaunchedOnce = true
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(25.dp)
            .background(color = iconColor, shape = RoundedCornerShape(5.dp)),
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = Color.Unspecified,
            modifier = Modifier.graphicsLayer(
                rotationZ = rotation.value,
                transformOrigin = TransformOrigin.Center,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainBottomBarPreview() {
    NapzakMarketTheme {
        var currentTab by remember { mutableStateOf(MainTab.HOME) }
        Box {
            MainBottomBar(
                isVisible = true,
                tabs = MainTab.entries.toImmutableList(),
                currentTab = currentTab,
                onTabSelected = { currentTab = it },
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}