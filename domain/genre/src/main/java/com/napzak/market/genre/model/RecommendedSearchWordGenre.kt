package com.napzak.market.genre.model

data class RecommendedSearchWordGenre(
    val searchWordList: List<SearchWord>,
    val genreList: List<Genre>,
) {
    data class SearchWord(
        val searchWordId: Long,
        val searchWord: String,
    )
}
