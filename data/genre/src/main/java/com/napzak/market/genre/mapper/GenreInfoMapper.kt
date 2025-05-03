package com.napzak.market.genre.mapper

import com.napzak.market.genre.dto.GenreInfoResponse
import com.napzak.market.genre.model.GenreInfo

fun GenreInfoResponse.toDomain(): GenreInfo =
    GenreInfo(
        genreId = genreId,
        genreName = genreName,
        tag = tag,
        cover = cover,
    )
