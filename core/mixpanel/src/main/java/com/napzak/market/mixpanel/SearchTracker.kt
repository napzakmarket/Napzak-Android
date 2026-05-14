package com.napzak.market.mixpanel

import javax.inject.Inject

/**
 * Service     : Search
 * Description : 검색 기능 관련 트래킹
 */
class SearchTracker @Inject constructor(
    private val analytics: NapzakAnalytics,
) {

    /**
     * Event       : Opened Search
     * Description : 검색 화면 진입
     */
    fun trackOpenedSearch() {
        analytics.logEvent(MixpanelConstants.OPENED_SEARCH)
    }

    /**
     * Event       : Executed Search
     * Description : 검색 실행
     * @param searchSource 검색 소스
     * @param keyword 검색어
     * @param genreName 장르명 (search_source가 genre_page일 때만 값 있음)
     */
    fun trackExecutedSearch(
        searchSource: String,
        keyword: String? = null,
        genreName: String? = null,
    ) {
        analytics.logEvent(
            MixpanelConstants.EXECUTED_SEARCH,
            mapOf(
                SEARCH_SOURCE to searchSource,
                SEARCH_TEXT to keyword,
                GENRE_NAME to genreName,
            ),
        )
    }

    /**
     * Event       : Clicked Suggestion
     * Description : 추천 검색어 클릭
     * @param suggestionType 추천 타입 (keyword, genre 등)
     * @param suggestionIndex 추천 순서
     */
    fun trackClickedSuggestion(suggestionType: String, suggestionIndex: Int) {
        analytics.logEvent(
            MixpanelConstants.CLICKED_SUGGESTION,
            mapOf(
                SUGGESTION_TYPE to suggestionType,
                SUGGESTION_INDEX to suggestionIndex,
            ),
        )
    }

    companion object {
        private const val SEARCH_SOURCE = "search_source"
        private const val SEARCH_TEXT = "keyword"
        private const val GENRE_NAME = "genre_name"
        private const val SUGGESTION_TYPE = "suggestion_type"
        private const val SUGGESTION_INDEX = "suggestion_index"

        const val SOURCE_ICON = "icon"
        const val SOURCE_ENTER = "enter"
        const val SOURCE_SEARCH_BAR = "searchbar"
        const val SOURCE_GENRE_PAGE = "genre_page"

        const val SUGGESTION_TYPE_KEYWORD = "keyword"
        const val SUGGESTION_TYPE_GENRE = "genre"
    }
}
