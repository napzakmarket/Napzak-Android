package com.napzak.market.mixpanel

import javax.inject.Inject

/**
 * Service     : Posting
 * Description : 게시글 작성 관련 트래킹
 */
class PostingTracker @Inject constructor(
    private val analytics: NapzakAnalytics,
) {

    /**
     * Event       : Created Post
     * Description : 게시글 작성 완료
     * @param postId 게시글 ID
     * @param postType 게시글 타입
     * @param genreName 장르명
     * @param userRole 유저 역할
     */
    fun trackCreatedPost(postId: Long, postType: String, genreName: String?, userRole: String) {
        analytics.logEvent(
            MixpanelConstants.CREATED_POST,
            mapOf(
                POST_ID to postId,
                POST_TYPE to postType,
                GENRES_CATEGORY to genreName,
                USER_ROLE to userRole,
            ),
        )
    }

    companion object {
        private const val POST_ID = "post_id"
        private const val POST_TYPE = "post_type"
        private const val GENRES_CATEGORY = "genres_category"
        private const val USER_ROLE = "user_role"

        const val POST_TYPE_FOR_SALE = "for_sale"
        const val POST_TYPE_WANTED = "wanted"
        const val USER_ROLE_SELLER = "seller"
        const val USER_ROLE_BUYER = "buyer"
    }
}
