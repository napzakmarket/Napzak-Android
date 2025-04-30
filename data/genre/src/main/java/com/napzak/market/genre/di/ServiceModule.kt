package com.napzak.market.genre.di

import com.napzak.market.genre.service.GenreService
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
    fun provideGenreService(retrofit: Retrofit): GenreService =
        retrofit.create()
}
