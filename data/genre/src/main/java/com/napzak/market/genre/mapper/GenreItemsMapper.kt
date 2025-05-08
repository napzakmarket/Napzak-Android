package com.napzak.market.genre.mapper

import com.napzak.market.genre.dto.GenreItemsResponse
import com.napzak.market.genre.dto.GenreResponse
import com.napzak.market.genre.model.Genre

fun GenreItemsResponse.toDomain(): Pair<List<Genre>, String?> =
    genreList.map { it.toGenre() } to nextCursor

fun GenreResponse.toGenre() =
    Genre(
        genreId = genreId,
        genreName = genreName,
        genrePhoto = genrePhoto,
    )
