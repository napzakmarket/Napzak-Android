package com.napzak.market.search.navigation

import com.napzak.market.navigation.EntryProviderBuilder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class SearchEntryProviderModule {

    @Binds
    @IntoSet
    @ActivityRetainedScoped
    abstract fun bindSearchEntryProvider(provider: SearchEntryProvider): EntryProviderBuilder
}
