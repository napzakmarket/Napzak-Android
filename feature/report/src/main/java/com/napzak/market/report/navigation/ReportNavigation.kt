package com.napzak.market.report.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.napzak.market.common.navigation.Route
import com.napzak.market.report.ReportRoute
import com.napzak.market.report.type.ReportType
import kotlinx.serialization.Serializable

private const val REPORT_TYPE_KEY = "reportType"

fun NavController.navigateToProductReport(productId: Long, navOptions: NavOptions? = null) =
    navigate(
        route = Report(id = productId, reportType = ReportType.PRODUCT.name),
        navOptions = navOptions,
    )

fun NavController.navigateToUserReport(userId: Long, navOptions: NavOptions? = null) =
    navigate(
        route = Report(id = userId, reportType = ReportType.USER.name),
        navOptions = navOptions,
    )

fun NavGraphBuilder.reportGraph(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    composable<Report> { backStackEntry ->
        val reportType =
            backStackEntry.arguments?.getString(REPORT_TYPE_KEY) ?: ReportType.PRODUCT.name

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
