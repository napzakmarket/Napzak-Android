package com.napzak.market.store.usecase

import com.napzak.market.store.model.Genre
import com.napzak.market.store.repository.StoreRepository
import javax.inject.Inject

class SetRegisterGenres @Inject constructor(
    private val storeRepository: StoreRepository,
) {
    suspend operator fun invoke(genreIds: List<Long>): Result<Genre> {
        return storeRepository.postRegisterGenres(genreIds)
    }
}