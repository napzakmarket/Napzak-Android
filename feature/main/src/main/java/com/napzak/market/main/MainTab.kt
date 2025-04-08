package com.napzak.market.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.napzak.market.common.navigation.MainTabRoute
import com.napzak.market.common.navigation.Route
import com.napzak.market.dummy.navigation.Dummy
import com.napzak.market.explore.navigation.Explore

// TODO: 구현 시작 시 수정
enum class MainTab(
    val icon: ImageVector,
    val contentDescription: String,
    val route: MainTabRoute,
) {
    DUMMY(
        icon = Icons.Filled.Home,
        contentDescription = "더미1",
        route = Dummy,
    ),
    EXPLORE(
        icon = Icons.Filled.Search,
        contentDescription = "탐색",
        route = Explore,
    );

    companion object {
        @Composable
        fun find(predicate: @Composable (MainTabRoute) -> Boolean): MainTab? {
            return entries.find { predicate(it.route) }
        }

        @Composable
        fun contains(predicate: @Composable (Route) -> Boolean): Boolean {
            return entries.map { it.route }.any { predicate(it) }
        }
    }
}