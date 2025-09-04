package com.napzak.market.mixpanel

import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject

fun MixpanelAPI?.trackEvent(name: String, props: Map<String, Any?> = emptyMap()) {
    this?.track(name, JSONObject().apply { props.forEach { (k, v) -> put(k, v) } })
}
