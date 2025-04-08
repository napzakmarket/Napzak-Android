package com.napzak.market.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.napzak.market.dummy.navigation.dummyGraph
import com.napzak.market.explore.navigation.exploreGraph
import com.napzak.market.main.component.MainBottomBar
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
) {
    Scaffold(
        bottomBar = {
            MainBottomBar(
                isVisible = navigator.showBottomBar(),
                tabs = MainTab.entries.toImmutableList(),
                currentTab = navigator.currentTab,
                onTabSelected = navigator::navigate
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            MainNavHost(
                navigator = navigator,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun MainNavHost(
    navigator: MainNavigator,
    modifier: Modifier = Modifier,
) {
    NavHost(
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        },
        popEnterTransition = {
            EnterTransition.None
        },
        popExitTransition = {
            ExitTransition.None
        },
        navController = navigator.navController,
        startDestination = navigator.startDestination
    ) {
        dummyGraph(modifier = modifier)

        exploreGraph(
            navigateToSearch = { /* TODO: 검색 화면으로 이동 */ },
            navigateToGenreDetail = { /* TODO: 장르 상세 화면으로 이동 */ },
            navigateToProductDetail = { /* TODO: 상품 상세 화면으로 이동 */ },
            modifier = modifier,
        )
    }
}