package com.napzak.market.genre.mapper

import com.napzak.market.genre.dto.PreferredGenreSearchItemsResponse
import com.napzak.market.genre.model.PreferredGenreSearchItems

fun PreferredGenreSearchItemsResponse.toDomain(): PreferredGenreSearchItems =
    PreferredGenreSearchItems(
        genreList = genreList.map { genre -> genre.toGenre() },
        nextCursor = nextCursor,
        externalLink = externalLink,
    )
