package com.napzak.market.genre.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendedSearchWordGenreResponse(
    @SerialName("searchWordList") val searchWordList: List<SearchWord>,
    @SerialName("genreList") val genreList: List<GenreResponse>,
) {
    @Serializable
    data class SearchWord(
        @SerialName("searchWordId") val searchWordId: Long,
        @SerialName("searchWord") val searchWord: String,
    )
}
