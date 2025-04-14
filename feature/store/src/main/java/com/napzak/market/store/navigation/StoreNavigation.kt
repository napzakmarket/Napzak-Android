package com.napzak.market.store.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.Route
import com.napzak.market.store.StoreRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToStore(
    storeId: Long,
    navOptions: NavOptions? = null,
) = navigate(Store(storeId), navOptions)

fun NavGraphBuilder.storeGraph(
    navigateToUp: () -> Unit,
    navigateToProfileEdit: () -> Unit,
    navigateToProductDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    composable<Store> {
        StoreRoute(
            onNavigateUp = navigateToUp,
            onProfileEditNavigate = navigateToProfileEdit,
            onProductDetailNavigate = navigateToProductDetail,
            modifier = modifier,
        )
    }
}

@Serializable
data class Store(
    val storeId: Long,
) : Route