package com.napzak.market.mixpanel

import javax.inject.Inject

/**
 * Service     : MyPage
 * Description : 마이페이지 관련 트래킹
 */
class MyPageTracker @Inject constructor(
    private val analytics: NapzakAnalytics,
) {

    /**
     * Event       : Viewed MyPage
     * Description : 마이페이지 진입
     */
    fun trackViewedMyPage() {
        analytics.logEvent(MixpanelConstants.VIEWED_MYPAGE)
    }
}
