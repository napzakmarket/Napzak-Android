package com.napzak.market.util.android

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation

/**
 * 화면 전환에 좌우 슬라이딩 애니메이션이 적용된 navigation 오버라이딩 함수입니다.
 *
 * @param startDestination 시작 화면
 * @param screens 화면 목록 (전환할 화면 순서대로 인덱싱이 돼야 합니다)
 * @param builder 화면 구성
 */
inline fun <reified T : Any, reified R : Any> NavGraphBuilder.horizontalSlideNavigation(
    startDestination: R,
    screens: List<R>,
    noinline builder: NavGraphBuilder.() -> Unit
) {
    val navigationSpec = tween<IntOffset>(durationMillis = 200)

    navigation<T>(
        startDestination = startDestination,
        enterTransition = {
            slideIntoContainer(
                towards = animationDirection(screens, initialState, targetState),
                animationSpec = navigationSpec,
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = animationDirection(screens, initialState, targetState),
                animationSpec = navigationSpec,
            )
        },
        builder = builder,
    )
}

inline fun <reified T : Any> animationDirection(
    screens: List<T>,
    initialState: NavBackStackEntry,
    targetState: NavBackStackEntry
) =
    if (initialState.screenOrder(screens) < targetState.screenOrder(screens)) {
        AnimatedContentTransitionScope.SlideDirection.Left
    } else {
        AnimatedContentTransitionScope.SlideDirection.Right
    }

inline fun <reified T : Any> NavBackStackEntry.screenOrder(screens: List<T>): Int {
    screens.forEachIndexed { index, screen ->
        if (destination.hasRoute(screen::class)) return index
    }
    return -1
}