package com.napzak.market.genre.mapper

import com.napzak.market.genre.dto.GenreItemsResponse
import com.napzak.market.genre.dto.GenreResponse
import com.napzak.market.genre.model.Genre

fun GenreItemsResponse.toModelList(): List<Genre> = genreList.toModelList()

fun List<GenreResponse>.toModelList(): List<Genre> = map { it.toModel() }

fun GenreResponse.toModel(): Genre =
    Genre(
        genreId = genreId,
        genreName = genreName,
        genrePhoto = genrePhoto,
        nextCursor = nextCursor
    )
