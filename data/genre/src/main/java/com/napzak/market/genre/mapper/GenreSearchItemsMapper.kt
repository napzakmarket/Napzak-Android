package com.napzak.market.genre.mapper

import com.napzak.market.genre.dto.GenreSearchItemsResponse
import com.napzak.market.genre.model.GenreSearchItems

fun GenreSearchItemsResponse.toDomain(): GenreSearchItems =
    GenreSearchItems(
        genreList = genreList.map { it.toGenre() },
        nextCursor = nextCursor,
        externalLink = externalLink,
    )
