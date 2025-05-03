package com.napzak.market.genre.di

import com.napzak.market.genre.repository.GenreInfoRepository
import com.napzak.market.genre.repository.GenreNameRepository
import com.napzak.market.genre.repository.PreferredGenreRepository
import com.napzak.market.genre.repository.SearchWordGenreRepository
import com.napzak.market.genre.repositoryimpl.GenreInfoRepositoryImpl
import com.napzak.market.genre.repositoryimpl.GenreNameRepositoryImpl
import com.napzak.market.genre.repositoryimpl.PreferredGenreRepositoryImpl
import com.napzak.market.genre.repositoryimpl.SearchWordGenreRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPreferredGenreRepository(
        preferredGenreRepositoryImpl: PreferredGenreRepositoryImpl,
    ): PreferredGenreRepository

    @Binds
    @Singleton
    abstract fun bindGenreNameRepository(
        genreNameRepositoryImpl: GenreNameRepositoryImpl,
    ): GenreNameRepository

    @Binds
    @Singleton
    abstract fun bindGenreInfoRepository(
        genreInfoRepositoryImpl: GenreInfoRepositoryImpl,
    ): GenreInfoRepository

    @Binds
    @Singleton
    abstract fun bindSearchWordGenreRepository(
        searchWordGenreRepositoryImpl: SearchWordGenreRepositoryImpl,
    ): SearchWordGenreRepository
}
