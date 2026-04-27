package com.napzak.market.main.navigation

import com.napzak.market.navigation.EntryProviderBuilder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.multibindings.Multibinds

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class NavigationModule {

    @Multibinds
    abstract fun bindEntryBuilderProviders(): Set<EntryProviderBuilder>

    @Binds
    @ActivityRetainedScoped
    abstract fun bindMainNavigator(impl: MainNavigatorImpl): MainNavigator
}
