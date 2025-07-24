package com.napzak.market.chat.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.chat.chatlist.ChatListRoute
import com.napzak.market.chat.chatroom.ChatRoomRoute
import com.napzak.market.common.navigation.MainTabRoute
import com.napzak.market.common.navigation.Route
import kotlinx.serialization.Serializable

fun NavHostController.navigateToChatList(
    navOptions: NavOptions? = null,
) = this.navigate(
    route = ChatList,
    navOptions = navOptions,
)

fun NavHostController.navigateToChatRoom(
    chatRoomId: Long? = null,
    productId: Long? = null,
    storeId: Long? = null,
    navOptions: NavOptions? = null,
) = this.navigate(
    route = ChatRoom(chatRoomId, productId, storeId),
    navOptions = navOptions,
)

fun NavGraphBuilder.chatGraph(
    onChatRoomNavigate: (Long) -> Unit,
    onProductDetailNavigate: (Long) -> Unit,
    onStoreReportNavigate: (Long) -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    composable<ChatList> {
        ChatListRoute(
            onChatRoomNavigate = onChatRoomNavigate,
            modifier = modifier,
        )
    }

    // TODO: 딥링크 추가
    composable<ChatRoom> {
        ChatRoomRoute(
            onProductDetailNavigate = onProductDetailNavigate,
            onStoreReportNavigate = onStoreReportNavigate,
            onNavigateUp = onNavigateUp,
        )
    }
}

@Serializable
data object ChatList : MainTabRoute

@Serializable
data class ChatRoom(
    val chatRoomId: Long?,
    val productId: Long?, //채팅목록창에서 접근하는 경우 productId를 null로 설정한다.
    val storeId: Long?,
) : Route
