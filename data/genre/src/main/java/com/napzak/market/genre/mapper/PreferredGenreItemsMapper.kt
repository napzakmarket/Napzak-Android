package com.napzak.market.genre.mapper

import com.napzak.market.genre.dto.PreferredGenreItemsResponse
import com.napzak.market.genre.model.Genre

fun PreferredGenreItemsResponse.toDomain(): Pair<List<Genre>, String?> =
    genreList.map { it.toGenre() } to nextCursor
