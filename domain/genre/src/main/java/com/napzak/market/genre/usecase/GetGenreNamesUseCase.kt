package com.napzak.market.genre.usecase

import com.napzak.market.genre.model.Genre
import com.napzak.market.genre.repository.GenreNameRepository
import javax.inject.Inject

class GetGenreNamesUseCase @Inject constructor(
    private val genreNameRepository: GenreNameRepository,
) {
    suspend operator fun invoke(searchTerm: String): Result<List<Genre>> =
        if (searchTerm.isBlank()) {
            genreNameRepository.getGenreNames().map { it.first }
        } else {
            genreNameRepository.getGenreNameResults(searchTerm).map { it.genreList }
        }
}
