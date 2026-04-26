package com.napzak.market.detail.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.napzak.market.common.type.ReportType
import com.napzak.market.common.type.TradeType
import com.napzak.market.detail.ProductDetailRoute
import com.napzak.market.detail.ProductDetailViewModel
import com.napzak.market.navigation.AppNavigator
import com.napzak.market.navigation.EntryProviderBuilder
import com.napzak.market.navigation.keys.ChatRoomScreenKey
import com.napzak.market.navigation.keys.ProductDetailScreenKey
import com.napzak.market.navigation.keys.PurchaseRegistrationScreenKey
import com.napzak.market.navigation.keys.ReportScreenKey
import com.napzak.market.navigation.keys.SaleRegistrationScreenKey
import com.napzak.market.navigation.keys.ScreenKey
import com.napzak.market.navigation.keys.StoreScreenKey
import com.napzak.market.navigation.util.assistedEntry
import javax.inject.Inject

class ProductDetailEntryProvider @Inject constructor(
    private val navigator: AppNavigator,
) : EntryProviderBuilder {
    override fun EntryProviderScope<ScreenKey>.provide() {
        assistedEntry<ProductDetailViewModel, ProductDetailViewModel.Factory, ProductDetailScreenKey> { _, viewModel ->
            ProductDetailRoute(
                onMarketNavigate = ::navigateToStore,
                onChatNavigate = ::navigateToChatRoom,
                onModifyNavigate = ::navigateToModify,
                onReportNavigate = ::navigateToProductReport,
                onNavigateUp = navigator::pop,
                viewModel = viewModel,
            )
        }
    }

    private fun navigateToStore(userId: Long) {
        navigator.navigateTo(StoreScreenKey(storeId = userId))
    }

    private fun navigateToChatRoom(productId: Long) {
        navigator.navigateTo(ChatRoomScreenKey(productId = productId))
    }

    private fun navigateToModify(productId: Long, tradeType: TradeType) {
        val screenKey = when (tradeType) {
            TradeType.SELL -> SaleRegistrationScreenKey(productId = productId)
            TradeType.BUY -> PurchaseRegistrationScreenKey(productId = productId)
        }
        navigator.navigateTo(screenKey)
    }

    private fun navigateToProductReport(productId: Long) {
        navigator.navigateTo(ReportScreenKey(reportType = ReportType.PRODUCT, id = productId))
    }
}
