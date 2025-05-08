package com.napzak.market.registration.event

import com.napzak.market.genre.model.Genre
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object GenreEventBus {
    private val _genreSelected = MutableSharedFlow<Genre>(extraBufferCapacity = 1)
    val genreSelected = _genreSelected.asSharedFlow()

    fun selectGenre(genre: Genre) = _genreSelected.tryEmit(genre)
}
