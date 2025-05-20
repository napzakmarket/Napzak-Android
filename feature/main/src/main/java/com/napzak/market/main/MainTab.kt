package com.napzak.market.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.napzak.market.common.navigation.MainTabRoute
import com.napzak.market.common.navigation.Route
import com.napzak.market.designsystem.R.drawable.ic_nav_add
import com.napzak.market.designsystem.R.drawable.ic_nav_chat
import com.napzak.market.designsystem.R.drawable.ic_nav_explore
import com.napzak.market.designsystem.R.drawable.ic_nav_home
import com.napzak.market.designsystem.R.drawable.ic_nav_user
import com.napzak.market.dummy.navigation.Dummy
import com.napzak.market.explore.navigation.Explore
import com.napzak.market.home.navigation.Home
import com.napzak.market.main.R.string.main_bottom_bar_chatting
import com.napzak.market.main.R.string.main_bottom_bar_explore
import com.napzak.market.main.R.string.main_bottom_bar_home
import com.napzak.market.main.R.string.main_bottom_bar_my_page
import com.napzak.market.main.R.string.main_bottom_bar_register
import com.napzak.market.mypage.navigation.MyPage


// TODO: 구현 시작 시 수정
enum class MainTab(
    @DrawableRes val iconRes: Int,
    @StringRes val title: Int,
    val route: MainTabRoute,
) {
    HOME(
        iconRes = ic_nav_home,
        title = main_bottom_bar_home,
        route = Home,
    ),
    EXPLORE(
        iconRes = ic_nav_explore,
        title = main_bottom_bar_explore,
        route = Explore(),
    ),
    REGISTER(
        iconRes = ic_nav_add,
        title = main_bottom_bar_register,
        route = Dummy,
    ),
    CHAT(
        iconRes = ic_nav_chat,
        title = main_bottom_bar_chatting,
        route = Dummy,
    ),
    MY_PAGE(
        iconRes = ic_nav_user,
        title = main_bottom_bar_my_page,
        route = MyPage,
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
