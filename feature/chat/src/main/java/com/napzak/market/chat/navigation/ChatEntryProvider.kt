package com.napzak.market.chat.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.napzak.market.chat.chatlist.ChatListRoute
import com.napzak.market.chat.chatroom.ChatRoomRoute
import com.napzak.market.chat.chatroom.ChatRoomViewModel
import com.napzak.market.common.type.ReportType
import com.napzak.market.navigation.AppNavigator
import com.napzak.market.navigation.EntryProviderBuilder
import com.napzak.market.navigation.keys.ChatListScreenKey
import com.napzak.market.navigation.keys.ChatRoomScreenKey
import com.napzak.market.navigation.keys.PhoneVerificationScreenKey
import com.napzak.market.mixpanel.ExploreTracker
import com.napzak.market.navigation.keys.ProductDetailScreenKey
import com.napzak.market.navigation.keys.ReportScreenKey
import com.napzak.market.navigation.keys.ScreenKey
import com.napzak.market.navigation.keys.SettingsScreenKey
import com.napzak.market.navigation.util.assistedEntry
import javax.inject.Inject

class ChatEntryProvider @Inject constructor(
    private val navigator: AppNavigator,
) : EntryProviderBuilder {
    override fun EntryProviderScope<ScreenKey>.provide() {
        entry<ChatListScreenKey> {
            ChatListRoute(
                onChatRoomNavigate = ::navigateToChatRoom,
                onSettingsNavigate = ::navigateToSettings,
            )
        }

        assistedEntry<ChatRoomViewModel, ChatRoomViewModel.Factory, ChatRoomScreenKey> { _, viewModel ->
            ChatRoomRoute(
                onProductDetailNavigate = ::navigateToProductDetail,
                onStoreReportNavigate = ::navigateToStoreReport,
                onPhoneVerificationNavigate = ::navigateToPhoneVerification,
                onNavigateUp = navigator::pop,
                viewModel = viewModel,
            )
        }
    }

    private fun navigateToChatRoom(chatRoomId: Long) {
        navigator.navigateTo(ChatRoomScreenKey(chatRoomId = chatRoomId))
    }

    private fun navigateToSettings() {
        navigator.navigateTo(SettingsScreenKey)
    }

    private fun navigateToProductDetail(productId: Long) {
        val screenKey = ProductDetailScreenKey(productId = productId, source = ExploreTracker.SOURCE_CHAT_ROOM)
        navigator.navigateTo(screenKey)
    }

    private fun navigateToStoreReport(userId: Long) {
        val screenKey = ReportScreenKey(reportType = ReportType.USER, id = userId)
        navigator.navigateTo(screenKey)
    }

    private fun navigateToPhoneVerification() {
        navigator.navigateTo(PhoneVerificationScreenKey(false))
    }
}
