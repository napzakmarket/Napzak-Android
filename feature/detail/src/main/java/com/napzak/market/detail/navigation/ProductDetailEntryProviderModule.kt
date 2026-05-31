package com.napzak.market.detail.navigation

import com.napzak.market.navigation.EntryProviderBuilder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ProductDetailEntryProviderModule {

    @Binds
    @IntoSet
    @ActivityRetainedScoped
    abstract fun bindDetailEntryProvider(provider: ProductDetailEntryProvider): EntryProviderBuilder
}
