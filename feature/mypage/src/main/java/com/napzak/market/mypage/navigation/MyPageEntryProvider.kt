package com.napzak.market.mypage.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.EntryProviderScope
import com.napzak.market.mixpanel.ExploreTracker
import com.napzak.market.mypage.mypage.MyPageRoute
import com.napzak.market.mypage.setting.SettingsRoute
import com.napzak.market.mypage.wishlist.WishlistRoute
import com.napzak.market.mypage.withdraw.WithdrawRoute
import com.napzak.market.navigation.AppNavigator
import com.napzak.market.navigation.EntryProviderBuilder
import com.napzak.market.navigation.keys.MyPageScreenKey
import com.napzak.market.navigation.keys.ProductDetailScreenKey
import com.napzak.market.navigation.keys.ScreenKey
import com.napzak.market.navigation.keys.SettingsScreenKey
import com.napzak.market.navigation.keys.StoreScreenKey
import com.napzak.market.navigation.keys.WishlistScreenKey
import com.napzak.market.navigation.keys.WithdrawScreenKey
import javax.inject.Inject

class MyPageEntryProvider @Inject constructor(
    private val navigator: AppNavigator,
) : EntryProviderBuilder {
    override fun EntryProviderScope<ScreenKey>.provide() {
        entry<MyPageScreenKey> {
            MyPageRoute(
                onMyMarketClick = ::navigateToStore,
                onSalesClick = { /* TODO: 판매내역 화면으로 이동 */ },
                onPurchaseClick = { /* TODO: 구매내역 화면으로 이동 */ },
                onRecentClick = { /* TODO: 최근 본 상품 화면으로 이동 */ },
                onFavoriteClick = ::navigateToWishlist,
                onSettingsClick = ::navigateToSettings,
            )
        }

        entry<SettingsScreenKey> {
            val context = LocalContext.current
            SettingsRoute(
                onBackClick = navigator::pop,
                onLogoutConfirm = { restartApplication(context) },
                onWithdrawClick = ::navigateToWithdraw,
            )
        }

        entry<WithdrawScreenKey> {
            val context = LocalContext.current
            WithdrawRoute(
                onNavigateUp = navigator::pop,
                onWithdrawComplete = { restartApplication(context) },
            )
        }

        entry<WishlistScreenKey> {
            WishlistRoute(
                onNavigateUp = navigator::pop,
                onProductDetailNavigate = ::navigateToProductDetail,
            )
        }
    }

    private fun navigateToStore(storeId: Long) {
        navigator.navigateTo(StoreScreenKey(storeId = storeId))
    }

    private fun navigateToSettings() {
        navigator.navigateTo(SettingsScreenKey)
    }

    private fun navigateToWithdraw() {
        navigator.navigateTo(WithdrawScreenKey)
    }

    private fun navigateToWishlist() {
        navigator.navigateTo(WishlistScreenKey)
    }

    private fun navigateToProductDetail(productId: Long) {
        navigator.navigateTo(ProductDetailScreenKey(productId = productId, source = ExploreTracker.SOURCE_WISH_LIST))
    }

    private fun restartApplication(context: Context) {
        with(context) {
            val intentComponent =
                packageManager.getLaunchIntentForPackage(packageName)?.component ?: return
            val intent = Intent.makeRestartActivityTask(intentComponent)
            startActivity(intent)
        }
    }
}
