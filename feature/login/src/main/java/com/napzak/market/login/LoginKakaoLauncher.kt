package com.napzak.market.login

import android.app.Activity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class LoginKakaoLauncher(
    private val activityProvider: () -> Activity?,
    private val onTokenReceived: (String) -> Unit,
    private val onError: (Throwable) -> Unit,
    private val onGuide: (Guide) -> Unit = {},
) {

    fun startKakaoLogin() {
        val activity = activityProvider() ?: run {
            onError(IllegalStateException("Activity is null")); return
        }
        val kakao = UserApiClient.instance

        if (kakao.isKakaoTalkLoginAvailable(activity)) {
            kakao.loginWithKakaoTalk(activity) { token, error ->
                when {
                    token != null -> onToken(token)
                    error is ClientError && error.reason == ClientErrorCause.Cancelled -> {
                        onError(error)
                    }

                    else -> {
                        loginWithKakaoAccount(activity)
                    }
                }
            }
        } else {
            loginWithKakaoAccount(activity)
        }
    }

    private fun loginWithKakaoAccount(activity: Activity) {
        UserApiClient.instance.loginWithKakaoAccount(activity) { token, error ->
            when {
                token != null -> onToken(token)
                error is ClientError && error.reason == ClientErrorCause.NotSupported -> {
                    onGuide(Guide.EnableChromeOrInstallKakaoTalk)
                }

                error != null -> onError(error)
                else -> onError(IllegalStateException("KakaoAccount: token/error both null"))
            }
        }
    }

    private fun onToken(token: OAuthToken) {
        onTokenReceived(token.accessToken)
    }

    enum class Guide { EnableChromeOrInstallKakaoTalk }
}