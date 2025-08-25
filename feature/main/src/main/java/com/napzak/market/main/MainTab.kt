package com.napzak.market.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.napzak.market.chat.navigation.ChatList
import com.napzak.market.common.navigation.MainTabRoute
import com.napzak.market.common.navigation.Route
import com.napzak.market.designsystem.R.drawable.ic_white_plus
import com.napzak.market.designsystem.R.drawable.ic_gray_chat
import com.napzak.market.designsystem.R.drawable.ic_gray_search
import com.napzak.market.designsystem.R.drawable.ic_gray_home
import com.napzak.market.designsystem.R.drawable.ic_gray_user
import com.napzak.market.explore.navigation.Explore
import com.napzak.market.home.navigation.Home
import com.napzak.market.main.R.string.main_bottom_bar_chatting
import com.napzak.market.main.R.string.main_bottom_bar_explore
import com.napzak.market.main.R.string.main_bottom_bar_home
import com.napzak.market.main.R.string.main_bottom_bar_my_page
import com.napzak.market.main.R.string.main_bottom_bar_register
import com.napzak.market.mypage.navigation.MyPage
import kotlinx.serialization.Serializable

enum class MainTab(
    @DrawableRes val iconRes: Int,
    @StringRes val title: Int,
    val route: MainTabRoute,
) {
    HOME(
        iconRes = ic_gray_home,
        title = main_bottom_bar_home,
        route = Home,
    ),
    EXPLORE(
        iconRes = ic_gray_search,
        title = main_bottom_bar_explore,
        route = Explore(),
    ),
    REGISTER(
        iconRes = ic_white_plus,
        title = main_bottom_bar_register,
        route = Registration(),
    ),
    CHAT(
        iconRes = ic_gray_chat,
        title = main_bottom_bar_chatting,
        route = ChatList,
    ),
    MY_PAGE(
        iconRes = ic_gray_user,
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

// TODO: Registration 관련 모듈로 이동
@Serializable
class Registration : MainTabRoute