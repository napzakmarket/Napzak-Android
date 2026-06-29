package com.napzak.market.home.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation3.runtime.EntryProviderScope
import com.napzak.market.common.type.SortType.POPULAR
import com.napzak.market.common.type.TradeType.BUY
import com.napzak.market.common.type.TradeType.SELL
import com.napzak.market.event.ChatSessionManager
import com.napzak.market.event.DeepLinkEvent
import com.napzak.market.event.DeepLinkEventBus
import com.napzak.market.event.ProductDetailSessionManager
import com.napzak.market.mixpanel.ExploreTracker
import com.napzak.market.home.HomeRoute
import com.napzak.market.navigation.AppNavigator
import com.napzak.market.navigation.EntryProviderBuilder
import com.napzak.market.navigation.keys.ExploreScreenKey
import com.napzak.market.navigation.keys.HomeScreenKey
import com.napzak.market.navigation.keys.PhoneVerificationScreenKey
import com.napzak.market.navigation.keys.ProductDetailScreenKey
import com.napzak.market.navigation.keys.ScreenKey
import com.napzak.market.navigation.keys.SearchScreenKey
import javax.inject.Inject

class HomeEntryProvider @Inject constructor(
    private val navigator: AppNavigator,
    private val chatSessionManager: ChatSessionManager,
    private val productDetailSessionManager: ProductDetailSessionManager,
    private val deepLinkEventBus: DeepLinkEventBus,
) : EntryProviderBuilder {
    override fun EntryProviderScope<ScreenKey>.provide() {
        entry<HomeScreenKey> {
            LaunchedEffect(Unit) {
                // HomeRoute 컴포지션 단계에서 채팅 알림 클릭 여부를 확인해 이벤트를 전달합니다.
                chatSessionManager.chatRoomId.value?.let { chatRoomId ->
                    deepLinkEventBus.send(DeepLinkEvent.NavigateToChatRoom(chatRoomId))
                }
                // 로그인 전 딥링크 진입 시 저장된 productId로 이동합니다.
                productDetailSessionManager.pendingProductId.value?.let { productId ->
                    deepLinkEventBus.send(DeepLinkEvent.NavigateToProductDetail(productId))
                }
            }

            HomeRoute(
                onSearchNavigate = ::navigateToSearch,
                onProductDetailNavigate = ::navigateToProductDetail,
                onPhoneVerificationNavigate = ::navigateToPhoneVerification,
                onMostInterestedSellNavigate = ::navigateToExploreSell,
                onMostInterestedBuyNavigate = ::navigateToExploreBuy,
            )
        }
    }

    private fun navigateToSearch() {
        navigator.navigateTo(SearchScreenKey)
    }

    private fun navigateToProductDetail(productId: Long) {
        navigator.navigateTo(ProductDetailScreenKey(productId, source = ExploreTracker.SOURCE_HOME_FEED))
    }

    private fun navigateToExploreBuy() {
        val screen = ExploreScreenKey(tradeType = BUY, sortType = POPULAR)
        navigator.navigateTo(screen)
    }

    private fun navigateToExploreSell() {
        val screen = ExploreScreenKey(tradeType = SELL, sortType = POPULAR)
        navigator.navigateTo(screen)
    }

    private fun navigateToPhoneVerification() {
        navigator.navigateTo(PhoneVerificationScreenKey(false))
    }
}
