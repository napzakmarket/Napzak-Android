package com.napzak.market.mixpanel

import javax.inject.Inject

/**
 * Service     : Settings
 * Description : 설정 관련 트래킹
 */
class SettingsTracker @Inject constructor(
    private val analytics: NapzakAnalytics,
) {

    /**
     * Event       : Viewed Settings
     * Description : 설정 화면 진입
     */
    fun trackViewedSetting() {
        analytics.logEvent(MixpanelConstants.VIEWED_SETTING)
    }

    /**
     * Event       : Toggled Alarm
     * Description : 알림 설정 변경 (ON/OFF)
     * @param status 알림 상태 (on, off)
     */
    fun trackToggledAlarm(status: String) {
        analytics.logEvent(
            MixpanelConstants.TOGGLED_ALARM,
            mapOf(STATUS to status),
        )
    }

    /**
     * Event       : Logged Out
     * Description : 로그아웃 실행
     */
    fun trackLoggedOut() {
        analytics.logEvent(MixpanelConstants.LOGGED_OUT)
    }

    /**
     * Event       : Started Withdrawal
     * Description : 서비스 탈퇴 시작
     */
    fun trackStartedWithdrawal() {
        analytics.logEvent(MixpanelConstants.STARTED_WITHDRAWAL)
    }

    /**
     * Event       : Completed Withdrawal
     * Description : 서비스 탈퇴 완료
     */
    fun trackCompletedWithdrawal() {
        analytics.logEvent(MixpanelConstants.COMPLETED_WITHDRAWAL)
    }

    companion object {
        private const val STATUS = "status"

        const val STATE_ON = "on"
        const val STATE_OFF = "off"
    }
}
