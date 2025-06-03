package com.napzak.market.ui_util

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation

/**
 * 화면 전환 시 좌우 슬라이딩 애니메이션이 적용된 커스텀 navigation 함수입니다.
 *
 * 온보딩, 튜토리얼 등 여러 화면을 순차적으로 구성하는 기능에 적합하며,
 * 다음과 같은 전환 효과를 제공합니다:
 *
 * - 다음 화면으로 이동 시: 현재 화면은 왼쪽으로 사라지고, 다음 화면은 오른쪽에서 슬라이드 인됩니다.
 * - 이전 화면으로 돌아갈 시: 현재 화면은 오른쪽으로 사라지고, 이전 화면은 왼쪽에서 슬라이드 인됩니다.
 */
inline fun <reified T : Any> NavGraphBuilder.horizontalSlideNavigation(
    startDestination: Any,
    noinline builder: NavGraphBuilder.() -> Unit,
) {
    val transitionDurationMillis = 300
    val enterTransition = slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(
            durationMillis = transitionDurationMillis,
            easing = LinearOutSlowInEasing
        )
    )
    val exitTransition = slideOutHorizontally(
        targetOffsetX = { -it },
        animationSpec = tween(
            durationMillis = transitionDurationMillis,
            easing = LinearOutSlowInEasing
        )
    )
    val popEnterTransition = slideInHorizontally(
        initialOffsetX = { -it },
        animationSpec = tween(
            durationMillis = transitionDurationMillis,
            easing = LinearOutSlowInEasing
        )
    )
    val popExitTransition = slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(
            durationMillis = transitionDurationMillis,
            easing = LinearOutSlowInEasing
        )
    )

    navigation<T>(
        startDestination = startDestination,
        enterTransition = { enterTransition },
        exitTransition = { exitTransition },
        popEnterTransition = { popEnterTransition },
        popExitTransition = { popExitTransition },
        builder = builder,
    )
}