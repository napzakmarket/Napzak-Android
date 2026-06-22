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

    /**
     * Event       : Item Status Updated
     * Description : 상품 상태(판매/구매 중, 예약 중, 판매/구매 완료) 변경
     * @param postId 게시글 ID
     * @param genreName 장르명
     * @param isForSale 판매글 여부
     * @param statusLabel 변경된 상태
     */
    fun trackItemStatusUpdated(
        postId: Long,
        genreName: String,
        isForSale: Boolean,
        statusLabel: String,
    ) {
        analytics.logEvent(
            MixpanelConstants.ITEM_STATUS_UPDATED,
            mapOf(
                POST_ID to postId,
                GENRE_NAME to genreName,
                TAB to if (isForSale) TAB_FOR_SALE else TAB_WANTED,
                STATUS_LABEL to statusLabel,
            ),
        )
    }

    companion object {
        private const val POST_ID = "post_id"
        private const val GENRE_NAME = "genre_name"
        private const val TAB = "tab"
        private const val STATUS_LABEL = "status_label"

        private const val TAB_FOR_SALE = "for_sale"
        private const val TAB_WANTED = "wanted"

        const val STATUS_ON_SALE = "on_sale"
        const val STATUS_RESERVED = "reserved"
        const val STATUS_COMPLETED = "completed"
    }
}
