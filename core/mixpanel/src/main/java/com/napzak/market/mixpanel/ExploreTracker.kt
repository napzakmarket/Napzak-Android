package com.napzak.market.mixpanel

import javax.inject.Inject

/**
 * Service     : Explore
 * Description : 둘러보기(탐색) 화면 관련 트래킹
 */
class ExploreTracker @Inject constructor(
    private val analytics: NapzakAnalytics,
) {

    /**
     * Event       : Viewed Explore
     * Description : 둘러보기 화면 조회
     * @param isForSale 판매탭 여부 (true: 판매글, false: 구함글)
     */
    fun trackViewedExplore(isForSale: Boolean) {
        analytics.logEvent(
            MixpanelConstants.VIEWED_EXPLORE,
            mapOf(
                TAB to if (isForSale) TAB_FOR_SALE else TAB_WANTED,
            ),
        )
    }

    /**
     * Event       : Applied Genre Filter
     * Description : 장르 필터 적용
     * @param filterCount 적용된 필터 개수
     * @param isForSale 현재 선택된 탭 정보
     */
    fun trackAppliedGenreFilter(filterCount: Int, isForSale: Boolean) {
        analytics.logEvent(
            MixpanelConstants.APPLIED_GENRE_FILTER,
            mapOf(
                FILTER_COUNT to filterCount,
                TAB to if (isForSale) TAB_FOR_SALE else TAB_WANTED,
            ),
        )
    }

    /**
     * Event       : Applied array Filter
     * Description : 정렬 필터 적용
     * @param sort 정렬 방식 (latest, popular 등)
     * @param isForSale 현재 선택된 탭 정보
     */
    fun trackAppliedArrayFilter(sort: String, isForSale: Boolean) {
        analytics.logEvent(
            MixpanelConstants.APPLIED_ARRAY_FILTER,
            mapOf(
                SORT to sort,
                TAB to if (isForSale) TAB_FOR_SALE else TAB_WANTED,
            ),
        )
    }

    /**
     * Event       : Viewed Product
     * Description : 탐색 화면에서 상품 상세 진입
     * @param postId 게시글 ID
     * @param postType 게시글 타입 (for_sale, wanted)
     */
    fun trackViewedProduct(postId: Long, postType: String) {
        analytics.logEvent(
            MixpanelConstants.VIEWED_PRODUCT,
            mapOf(
                POST_ID to postId,
                POST_TYPE to postType,
            ),
        )
    }

    /**
     * Event       : Started Chat
     * Description : 탐색 화면에서 채팅 시작
     * @param postId 게시글 ID
     * @param postType 게시글 타입
     * @param userRole 유저 역할 (buyer, seller)
     */
    fun trackStartedChatFromExplore(postId: Long, postType: String, userRole: String) {
        analytics.logEvent(
            MixpanelConstants.STARTED_CHAT,
            mapOf(
                POST_ID to postId,
                POST_TYPE to postType,
                USER_ROLE to userRole,
            ),
        )
    }

    companion object {
        private const val TAB = "tab"
        private const val FILTER_COUNT = "filter_count"
        private const val SORT = "sort"
        private const val POST_ID = "post_id"
        private const val POST_TYPE = "post_type"
        private const val USER_ROLE = "user_role"

        private const val TAB_FOR_SALE = "for_sale"
        private const val TAB_WANTED = "wanted"

        const val SORT_LATEST = "latest"
        const val SORT_POPULAR = "popular"
        const val SORT_HIGH_PRICE = "high_price"
        const val SORT_LOW_PRICE = "low_price"

        const val POST_TYPE_FOR_SALE = "for_sale"
        const val POST_TYPE_WANTED = "wanted"

        const val USER_ROLE_BUYER = "buyer"
        const val USER_ROLE_SELLER = "seller"
    }
}
