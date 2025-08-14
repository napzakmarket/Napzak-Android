package com.napzak.market.detail.navigation

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.Route
import com.napzak.market.common.type.TradeType
import com.napzak.market.detail.ProductDetailRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToProductDetail(
    productId: Long,
    navOptions: NavOptions? = null,
) = navigate(
    route = ProductDetail(productId),
    navOptions = navOptions,
)

fun NavGraphBuilder.productDetailGraph(
    onMarketNavigate: (userId: Long) -> Unit,
    onChatNavigate: (productId: Long) -> Unit,
    onModifyNavigate: (productId: Long, tradeType: TradeType) -> Unit,
    onReportNavigate: (productId: Long) -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    composable<ProductDetail> {
        ProductDetailRoute(
            onMarketNavigate = onMarketNavigate,
            onChatNavigate = onChatNavigate,
            onNavigateUp = onNavigateUp,
            onModifyNavigate = onModifyNavigate,
            onReportNavigate = onReportNavigate,
            modifier = modifier,
        )
    }
}

@Serializable
data class ProductDetail(
    val productId: Long,
) : Route
