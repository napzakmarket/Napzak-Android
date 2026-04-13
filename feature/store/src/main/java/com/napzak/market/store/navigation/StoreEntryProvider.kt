package com.napzak.market.store.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.napzak.market.common.type.ReportType
import com.napzak.market.mixpanel.ExploreTracker
import com.napzak.market.navigation.AppNavigator
import com.napzak.market.navigation.EntryProviderBuilder
import com.napzak.market.navigation.keys.EditStoreScreenKey
import com.napzak.market.navigation.keys.ProductDetailScreenKey
import com.napzak.market.navigation.keys.ReportScreenKey
import com.napzak.market.navigation.keys.ScreenKey
import com.napzak.market.navigation.keys.StoreScreenKey
import com.napzak.market.navigation.util.assistedEntry
import com.napzak.market.store.edit_store.EditStoreRoute
import com.napzak.market.store.store.StoreRoute
import com.napzak.market.store.store.StoreViewModel
import javax.inject.Inject

class StoreEntryProvider @Inject constructor(
    private val navigator: AppNavigator,
) : EntryProviderBuilder {
    override fun EntryProviderScope<ScreenKey>.provide() {
        assistedEntry<StoreViewModel, StoreViewModel.Factory, StoreScreenKey> { _, viewModel ->
            StoreRoute(
                onNavigateUp = navigator::pop,
                onProfileEditNavigate = ::navigateToEditStore,
                onProductDetailNavigate = ::navigateToProductDetail,
                onStoreReportNavigate = ::navigateToStoreReport,
                viewModel = viewModel,
            )
        }

        entry<EditStoreScreenKey> {
            EditStoreRoute(
                onNavigateUp = navigator::pop,
            )
        }
    }

    private fun navigateToEditStore(storeId: Long) {
        navigator.navigateTo(EditStoreScreenKey(storeId = storeId))
    }

    private fun navigateToProductDetail(productId: Long) {
        navigator.navigateTo(ProductDetailScreenKey(productId = productId, source = ExploreTracker.SOURCE_MY_PAGE))
    }

    private fun navigateToStoreReport(userId: Long) {
        navigator.navigateTo(ReportScreenKey(reportType = ReportType.USER, id = userId))
    }
}
