package com.napzak.market.genre.mapper

import com.napzak.market.genre.dto.GenreItemsResponse
import com.napzak.market.genre.dto.GenreResponse
import com.napzak.market.genre.model.Genre

fun GenreItemsResponse.toGenres() = genreList.map { it.toGenre() }

fun GenreResponse.toGenre() = with(this) {
    Genre(
        genreId = genreId,
        genreName = genreName,
        genrePhoto = genrePhoto,
        nextCursor = nextCursor
    )
}
