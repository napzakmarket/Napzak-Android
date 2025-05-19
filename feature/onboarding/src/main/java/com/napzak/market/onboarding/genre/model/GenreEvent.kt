package com.napzak.market.onboarding.genre.model

sealed class GenreEvent {
    object MaxSelectionReached : GenreEvent()
}