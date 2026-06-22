package com.napzak.market.mixpanel

import javax.inject.Inject

/**
 * Service     : Home
 * Description : 홈 화면 관련 트래킹
 */
class HomeTracker @Inject constructor(
    private val analytics: NapzakAnalytics,
) {

    /**
     * Event       : Clicked Banner
     * Description : 홈 상단/미니 배너 클릭
     * @param bannerId 배너 ID
     * @param bannerType 배너 타입 (main, mini 등)
     * @param bannerIndex 배너 위치 순서
     */
    fun trackClickedBanner(bannerId: Long, bannerType: String, bannerIndex: Int) {
        analytics.logEvent(
            MixpanelConstants.CLICKED_BANNER,
            mapOf(
                BANNER_ID to bannerId,
                BANNER_TYPE to bannerType,
                BANNER_INDEX to bannerIndex,
            ),
        )
    }

    /**
     * Event       : Clicked custom genre
     * Description : 홈 화면 추천 상품 섹션 클릭 (기존 상수명 유지)
     * @param itemIndex 아이템 위치 순서
     * @param postId 상품 ID
     * @param genreName 장르 이름
     */
    fun trackClickedRecommendProduct(itemIndex: Int, postId: Long, genreName: String) {
        analytics.logEvent(
            MixpanelConstants.CLICKED_CUSTOM_GENRE,
            mapOf(
                ITEM_INDEX to itemIndex,
                POST_ID to postId,
                GENRE_NAME to genreName,
            ),
        )
    }

    /**
     * Event       : Viewed Popular For Sale
     * Description : 인기 판매중 아이템 섹션 조회
     */
    fun trackViewedPopularSale() {
        analytics.logEvent(
            MixpanelConstants.VIEWED_POPULAR_SALE,
            mapOf(
                SORT to POPULAR,
                FROM to HOME,
            ),
        )
    }

    /**
     * Event       : Viewed Popular Wanted
     * Description : 인기 구함중 아이템 섹션 조회
     */
    fun trackViewedPopularWanted() {
        analytics.logEvent(
            MixpanelConstants.VIEWED_POPULAR_WANTED,
            mapOf(
                SORT to POPULAR,
                FROM to HOME,
            ),
        )
    }

    companion object {
        private const val BANNER_ID = "banner_id"
        private const val BANNER_TYPE = "banner_type"
        private const val BANNER_INDEX = "banner_index"
        private const val ITEM_INDEX = "item_index"
        private const val POST_ID = "post_id"
        private const val GENRE_NAME = "genre_name"
        private const val SORT = "sort"
        private const val FROM = "from"
        private const val HOME = "home"
        private const val POPULAR = "popular"

        const val BANNER_MAIN = "main"
        const val BANNER_MINI = "mini"
    }
}
