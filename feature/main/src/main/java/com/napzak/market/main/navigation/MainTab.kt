package com.napzak.market.main.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.napzak.market.designsystem.R.drawable.ic_gray_chat
import com.napzak.market.designsystem.R.drawable.ic_gray_home
import com.napzak.market.designsystem.R.drawable.ic_gray_search
import com.napzak.market.designsystem.R.drawable.ic_gray_user
import com.napzak.market.designsystem.R.drawable.ic_white_plus
import com.napzak.market.main.R.string.main_bottom_bar_chatting
import com.napzak.market.main.R.string.main_bottom_bar_explore
import com.napzak.market.main.R.string.main_bottom_bar_home
import com.napzak.market.main.R.string.main_bottom_bar_my_page
import com.napzak.market.main.R.string.main_bottom_bar_register
import com.napzak.market.navigation.keys.ChatListScreenKey
import com.napzak.market.navigation.keys.ExploreScreenKey
import com.napzak.market.navigation.keys.HomeScreenKey
import com.napzak.market.navigation.keys.MainTabScreenKey
import com.napzak.market.navigation.keys.MyPageScreenKey
import com.napzak.market.navigation.keys.RegistrationScreenKey
import com.napzak.market.navigation.keys.ScreenKey

enum class MainTab(
    @DrawableRes val iconRes: Int,
    @StringRes val title: Int,
    val route: MainTabScreenKey,
) {
    HOME(
        iconRes = ic_gray_home,
        title = main_bottom_bar_home,
        route = HomeScreenKey,
    ),
    EXPLORE(
        iconRes = ic_gray_search,
        title = main_bottom_bar_explore,
        route = ExploreScreenKey(),
    ),
    REGISTER(
        iconRes = ic_white_plus,
        title = main_bottom_bar_register,
        route = RegistrationScreenKey,
    ),
    CHAT(
        iconRes = ic_gray_chat,
        title = main_bottom_bar_chatting,
        route = ChatListScreenKey,
    ),
    MY_PAGE(
        iconRes = ic_gray_user,
        title = main_bottom_bar_my_page,
        route = MyPageScreenKey,
    );

    companion object {
        fun find(predicate: (MainTabScreenKey) -> Boolean): MainTab? {
            return MainTab.entries.find { predicate(it.route) }
        }

        fun contains(predicate: (ScreenKey) -> Boolean): Boolean {
            return MainTab.entries.map { it.route as ScreenKey }.any { predicate(it) }
        }
    }
}
