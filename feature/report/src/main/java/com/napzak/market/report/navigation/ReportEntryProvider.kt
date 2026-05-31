package com.napzak.market.report.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.napzak.market.navigation.AppNavigator
import com.napzak.market.navigation.EntryProviderBuilder
import com.napzak.market.navigation.keys.ReportScreenKey
import com.napzak.market.navigation.keys.ScreenKey
import com.napzak.market.navigation.util.assistedEntry
import com.napzak.market.report.ReportRoute
import com.napzak.market.report.ReportViewModel
import javax.inject.Inject

class ReportEntryProvider @Inject constructor(
    private val navigator: AppNavigator,
) : EntryProviderBuilder {
    override fun EntryProviderScope<ScreenKey>.provide() {
        assistedEntry<ReportViewModel, ReportViewModel.Factory, ReportScreenKey> { key, viewModel ->
            ReportRoute(
                reportType = key.reportType,
                navigateUp = navigator::pop,
                viewModel = viewModel,
            )
        }
    }


}
