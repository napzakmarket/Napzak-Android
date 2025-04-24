package com.napzak.market.store.edit_store.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.Route
import com.napzak.market.store.edit_store.EditStoreRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToEditStore(
    storeId: Long,
    navOptions: NavOptions? = null,
) = navigate(EditStore(storeId), navOptions)

fun NavGraphBuilder.editStoreGraph(
    navigateToUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    composable<EditStore> {
        EditStoreRoute(
            onNavigateUp = navigateToUp,
            modifier = modifier,
        )
    }
}

@Serializable
data class EditStore(
    val storeId: Long,
) : Route