package com.napzak.market.wishlist.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.Route
import com.napzak.market.wishlist.WishlistRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToWishlist(navOptions: NavOptions? = null) =
    navigate(Wishlist, navOptions)

fun NavGraphBuilder.wishlistGraph(
    navigateToUp: () -> Unit,
    navigateToProductDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    composable<Wishlist> {
        WishlistRoute(
            onNavigateUp = navigateToUp,
            onProductDetailNavigate = navigateToProductDetail,
            modifier = modifier,
        )
    }
}

@Serializable
data object Wishlist : Route
