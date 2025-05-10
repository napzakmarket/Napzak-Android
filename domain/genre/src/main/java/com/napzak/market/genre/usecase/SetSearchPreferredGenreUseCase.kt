package com.napzak.market.genre.usecase

import com.napzak.market.genre.model.Genre
import com.napzak.market.genre.repository.PreferredGenreRepository
import javax.inject.Inject

class SetSearchPreferredGenresUseCase @Inject constructor(
    private val preferredGenreRepository: PreferredGenreRepository
) {
    suspend operator fun invoke(query: String): Result<List<Genre>> {
        return preferredGenreRepository
            .getPreferredGenreResults(searchWord = query)
            .map { it.genreList }
    }
}