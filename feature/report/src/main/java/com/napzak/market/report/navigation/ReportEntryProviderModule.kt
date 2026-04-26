package com.napzak.market.report.navigation

import com.napzak.market.navigation.EntryProviderBuilder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ReportEntryProviderModule {

    @Binds
    @IntoSet
    @ActivityRetainedScoped
    abstract fun bindReportEntryProvider(provider: ReportEntryProvider): EntryProviderBuilder
}
