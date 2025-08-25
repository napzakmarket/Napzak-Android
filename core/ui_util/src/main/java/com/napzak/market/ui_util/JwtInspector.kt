package com.napzak.market.ui_util

import android.util.Base64
import org.json.JSONObject

object JwtInspector {
    fun extractRoleString(token: String?): String? = runCatching {
        if (token.isNullOrBlank()) return null
        val payload = token.split(".").getOrNull(1) ?: return null
        val json = String(Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP))
        JSONObject(json).optString("role", null)
    }.getOrNull()
}