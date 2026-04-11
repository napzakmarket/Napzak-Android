package com.napzak.market.mixpanel

import javax.inject.Inject

/**
 * Service     : Onboarding
 * Description : 온보딩 및 회원가입 관련 트래킹
 */
class OnboardingTracker @Inject constructor(
    private val analytics: NapzakAnalytics,
) {

    /**
     * Event       : Signed Up
     * Description : 로그인 완료
     */
    fun trackSignedUp() {
        analytics.logEvent(MixpanelConstants.SIGNED_UP)
    }

    /**
     * Event       : Skipped Genres
     * Description : 관심 장르 선택 건너뛰기 클릭
     */
    fun trackSkippedGenres() {
        analytics.logEvent(MixpanelConstants.SKIPPED_GENRES)
    }

    /**
     * Event       : Completed Onboarding
     * Description : 온보딩 완료
     * @param method 가입 수단
     * @param genresCount 선택된 장르 개수
     * @param genresCategory 선택된 장르 리스트
     */
    fun trackCompletedOnboarding(
        method: String,
        genresCount: Int,
        genresCategory: List<String>,
    ) {
        analytics.logEvent(
            MixpanelConstants.COMPLETED_ONBOARDING,
            mapOf(
                METHOD to method,
                PLATFORM to PLATFORM_ANDROID,
                GENRES_COUNT to genresCount,
                GENRES_CATEGORY to genresCategory,
            ),
        )
    }

    companion object {
        private const val METHOD = "method"
        private const val PLATFORM = "platform"
        private const val GENRES_COUNT = "genres_selected_count"
        private const val GENRES_CATEGORY = "genres_category"

        private const val PLATFORM_ANDROID = "android"

        const val METHOD_KAKAO = "kakao"
    }
}
