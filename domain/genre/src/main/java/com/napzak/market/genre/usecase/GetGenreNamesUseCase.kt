package com.napzak.market.genre.usecase

import com.napzak.market.genre.model.Genre
import com.napzak.market.genre.repository.GenreNameRepository
import javax.inject.Inject

class GetGenreNamesUseCase @Inject constructor(
    private val genreNameRepository: GenreNameRepository,
) {
    suspend operator fun invoke(searchWord: String): Result<List<Genre>> {
        val genres = if (searchWord.isBlank()) {
            genreNameRepository.getGenreNames().getOrThrow().first
        } else {
            genreNameRepository.getGenreNameResults(searchWord).getOrThrow().genreList
        }

        return Result.success(genres)
    }
}