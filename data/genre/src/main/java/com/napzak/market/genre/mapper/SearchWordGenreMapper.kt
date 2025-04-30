package com.napzak.market.genre.mapper

import com.napzak.market.genre.dto.RecommendedSearchWordGenreResponse
import com.napzak.market.genre.model.Genre
import com.napzak.market.genre.model.RecommendedSearchWordGenre

fun RecommendedSearchWordGenreResponse.toModel(): RecommendedSearchWordGenre =
    RecommendedSearchWordGenre(
        searchWordList = searchWordList.map {
            RecommendedSearchWordGenre.SearchWord(
                searchWordId = it.searchWordId,
                searchWord = it.searchWord
            )
        },
        genreList = genreList.map {
            Genre(
                genreId = it.genreId,
                genreName = it.genreName,
                genrePhoto = it.genrePhoto,
                nextCursor = it.nextCursor
            )
        }
    )
