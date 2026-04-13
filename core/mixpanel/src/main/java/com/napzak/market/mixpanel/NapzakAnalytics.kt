package com.napzak.market.mixpanel

import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NapzakAnalytics @Inject constructor(
    private val mixpanelApi: MixpanelAPI,
) {
    fun logEvent(name: String, params: Map<String, Any?> = emptyMap()) {
        mixpanelApi.track(
            name,
            JSONObject().apply {
                params.forEach { (key, value) -> put(key, value) }
            },
        )
    }
}
