package com.napzak.market.chat.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.chat.chatroom.ChatRoomRoute
import com.napzak.market.common.navigation.Route
import kotlinx.serialization.Serializable

fun NavHostController.navigateToChatRoom(
    chatRoomId: Long,
    productId: Long? = null,
    navOptions: NavOptions? = null
) = this.navigate(
    route = ChatRoom(chatRoomId, productId),
    navOptions = navOptions
)

fun NavGraphBuilder.chatGraph(
    onProductDetailNavigate: (Long) -> Unit,
    onStoreReportNavigate: (Long) -> Unit,
    onNavigateUp: () -> Unit,
) {
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
data class ChatRoom(
    val chatRoomId: Long,
    val productId: Long?, //채팅목록창에서 접근하는 경우 productId를 null로 설정한다.
) : Route
