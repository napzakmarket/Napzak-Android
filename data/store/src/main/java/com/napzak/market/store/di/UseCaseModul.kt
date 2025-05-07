package com.napzak.market.store.di

import com.napzak.market.store.repository.StoreRepository
import com.napzak.market.store.usecase.CheckNicknameDuplicationUseCase
import com.napzak.market.store.usecase.SetNicknameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideCheckNicknameDuplicationUseCase(
        storeRepository: StoreRepository,
    ): CheckNicknameDuplicationUseCase {
        return CheckNicknameDuplicationUseCase(storeRepository)
    }

    @Provides
    fun provideSetNicknameUseCase(
        storeRepository: StoreRepository,
    ): SetNicknameUseCase {
        return SetNicknameUseCase(storeRepository)
    }
}