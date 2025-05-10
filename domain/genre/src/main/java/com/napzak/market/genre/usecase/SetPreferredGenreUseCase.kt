package com.napzak.market.genre.usecase

import com.napzak.market.genre.model.Genre
import com.napzak.market.genre.repository.PreferredGenreRepository
import javax.inject.Inject

class SetPreferredGenreUseCase @Inject constructor(
    private val preferredGenreRepository: PreferredGenreRepository,
) {
    suspend operator fun invoke(cursor: String? = null): Result<List<Genre>> {
        return preferredGenreRepository
            .getPreferredGenres(cursor)
            .map { (genres, _) -> genres }
    }
}