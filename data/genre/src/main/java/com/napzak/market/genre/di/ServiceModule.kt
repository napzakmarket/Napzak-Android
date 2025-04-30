package com.napzak.market.genre.di

import com.napzak.market.genre.service.GenreInfoService
import com.napzak.market.genre.service.GenreNameService
import com.napzak.market.genre.service.PreferredGenreService
import com.napzak.market.genre.service.SearchWordGenreService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun providePreferredGenreService(retrofit: Retrofit): PreferredGenreService =
        retrofit.create()

    @Provides
    @Singleton
    fun provideGenreNameService(retrofit: Retrofit): GenreNameService =
        retrofit.create()

    @Provides
    @Singleton
    fun provideGenreInfoService(retrofit: Retrofit): GenreInfoService =
        retrofit.create()

    @Provides
    @Singleton
    fun provideSearchWordGenreService(retrofit: Retrofit): SearchWordGenreService =
        retrofit.create()
}
