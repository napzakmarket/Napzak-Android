package com.napzak.market.dummy.di

import com.napzak.market.dummy.service.DummyService
import com.napzak.market.remote.qualifier.DUMMY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DummyServiceModule {

    @Provides
    @Singleton
    fun provideDummyService(@DUMMY retrofit: Retrofit): DummyService = retrofit.create()
}