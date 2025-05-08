package com.napzak.market.genre.datasource

import com.napzak.market.genre.service.SearchWordGenreService
import javax.inject.Inject

class SearchWordGenreDataSource @Inject constructor(
    private val searchWordGenreService: SearchWordGenreService,
) {
    suspend fun getRecommendedSearchWordGenres() =
        searchWordGenreService.getRecommendedSearchWordGenres()
}
