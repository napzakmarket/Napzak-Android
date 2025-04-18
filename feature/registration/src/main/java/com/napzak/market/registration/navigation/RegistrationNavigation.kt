package com.napzak.market.registration.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.Route
import com.napzak.market.registration.purchase.PurchaseRegistrationRoute
import com.napzak.market.registration.sale.SaleRegistrationRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToSaleRegistration(navOptions: NavOptions? = null) = navigate(SaleRegistration, navOptions)

fun NavController.navigateToPurchaseRegistration(navOptions: NavOptions? = null) = navigate(PurchaseRegistration, navOptions)

fun NavGraphBuilder.registrationGraph(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    composable<SaleRegistration> {
        PurchaseRegistrationRoute(
            onCloseClick = navigateUp,
            modifier = modifier,
        )
    }
    composable<PurchaseRegistration> {
        SaleRegistrationRoute(
            onCloseClick = navigateUp,
            modifier = modifier,
        )
    }
}

@Serializable
data object SaleRegistration : Route

@Serializable
data object PurchaseRegistration : Route