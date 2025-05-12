package com.napzak.market.login

import android.content.Context
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.user.UserApiClient

class LoginKakaoLauncher(
    private val context: Context,
    private val onTokenReceived: (String) -> Unit,
    private val onError: (Throwable) -> Unit,
) {
    fun startKakaoLogin() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (token != null) {
                    onTokenReceived(token.accessToken)
                } else if (error != null && error !is ClientError) {
                    loginWithKakaoAccount()
                }
            }
        } else {
            loginWithKakaoAccount()
        }
    }

    private fun loginWithKakaoAccount() {
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            if (token != null) {
                onTokenReceived(token.accessToken)
            } else if (error != null) {
                onError(error)
            }
        }
    }
}