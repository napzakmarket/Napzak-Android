package com.napzak.market.report.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.Route
import com.napzak.market.report.ReportRoute
import kotlinx.serialization.Serializable

private const val REPORT_TYPE_KEY = "reportType"
private const val PRODUCT_REPORT = "PRODUCT"
private const val USER_REPORT = "USER"

fun NavController.navigateToProductReport(productId: Long, navOptions: NavOptions? = null) =
    navigate(
        route = Report(id = productId, reportType = PRODUCT_REPORT),
        navOptions = navOptions,
    )

fun NavController.navigateToUserReport(userId: Long, navOptions: NavOptions? = null) =
    navigate(
        route = Report(id = userId, reportType = USER_REPORT),
        navOptions = navOptions,
    )

fun NavGraphBuilder.reportGraph(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    composable<Report> { backStackEntry ->
        val reportType = backStackEntry.arguments?.getString(REPORT_TYPE_KEY) ?: PRODUCT_REPORT

        ReportRoute(
            reportType = reportType,
            navigateUp = navigateUp,
            modifier = modifier,
        )
    }
}

@Serializable
data class Report(
    val reportType: String,
    val id: Long,
) : Route
