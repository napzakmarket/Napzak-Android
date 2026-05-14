package com.napzak.market.mixpanel

import javax.inject.Inject

/**
 * Service     : Global
 * Description : 여러 페이지에서 공통으로 발생하는 액션 트래킹
 */
class GlobalTracker @Inject constructor(
    private val analytics: NapzakAnalytics,
) {

    /**
     * Event       : Item Liked
     * Description : 상품 찜(좋아요) 추가/취소
     * @param postId 게시글 ID
     * @param genreName 장르명
     * @param isForSale 판매글 여부
     * @param source 찜 액션 발생 화면
     * @param actionType add / remove
     */
    fun trackItemLiked(
        postId: Long,
        genreName: String,
        isForSale: Boolean,
        source: String,
        actionType: String,
    ) {
        analytics.logEvent(
            MixpanelConstants.ITEM_LIKED,
            mapOf(
                POST_ID to postId,
                GENRE_NAME to genreName,
                TAB to if (isForSale) TAB_FOR_SALE else TAB_WANTED,
                SOURCE to source,
                ACTION_TYPE to actionType,
            ),
        )
    }

    companion object {
        private const val POST_ID = "post_id"
        private const val GENRE_NAME = "genre_name"
        private const val TAB = "tab"
        private const val SOURCE = "source"
        private const val ACTION_TYPE = "action_type"

        private const val TAB_FOR_SALE = "for_sale"
        private const val TAB_WANTED = "wanted"

        const val SOURCE_HOME_FEED = "home_feed"
        const val SOURCE_EXPLORE_FEED = "explore_feed"
        const val SOURCE_SEARCH_RESULT = "search_result"
        const val SOURCE_GENRE_PAGE = "genre_page"
        const val SOURCE_ITEM_DETAIL = "item_detail"

        const val ACTION_ADD = "add"
        const val ACTION_REMOVE = "remove"
    }
}
