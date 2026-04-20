package com.napzak.market.mixpanel

import javax.inject.Inject

/**
 * Service     : Report / Detail Action
 * Description : 신고 및 상품 상세 화면 주요 액션(상태 변경 등) 관련 트래킹
 */
class ReportTracker @Inject constructor(
    private val analytics: NapzakAnalytics,
) {

    /**
     * Event       : Opened Report Overlay_product
     * Description : 상품 신고 바텀시트/오버레이 노출
     */
    fun trackOpenedReportProduct() {
        analytics.logEvent(MixpanelConstants.OPENED_REPORT_PRODUCT)
    }

    /**
     * Event       : Opened Report Overlay_market
     * Description : 마켓(채팅/상점) 신고 바텀시트/오버레이 노출
     */
    fun trackOpenedReportMarket() {
        analytics.logEvent(MixpanelConstants.OPENED_REPORT_MARKET)
    }
}
