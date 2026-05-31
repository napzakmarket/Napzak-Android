package com.napzak.market.login.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.napzak.market.event.PhoneVerificationSessionManager
import com.napzak.market.event.SocketEventBus
import com.napzak.market.login.LoginRoute
import com.napzak.market.login.model.LoginFlowRoute
import com.napzak.market.navigation.AppNavigator
import com.napzak.market.navigation.EntryProviderBuilder
import com.napzak.market.navigation.keys.HomeScreenKey
import com.napzak.market.navigation.keys.LoginScreenKey
import com.napzak.market.navigation.keys.ScreenKey
import com.napzak.market.navigation.keys.TermsScreenKey
import javax.inject.Inject

class LoginEntryProvider @Inject constructor(
    private val navigator: AppNavigator,
    private val socketEventBus: SocketEventBus,
    private val phoneVerificationSessionManager: PhoneVerificationSessionManager,
) : EntryProviderBuilder {
    override fun EntryProviderScope<ScreenKey>.provide() {
        entry<LoginScreenKey> {
            LoginRoute(
                onRouteChanged = ::onRouteChanged,
            )
        }
    }

    private fun onRouteChanged(route: LoginFlowRoute) {
        when (route) {
            is LoginFlowRoute.Terms -> navigateToTerms()
            is LoginFlowRoute.Main -> navigateToMain()
            is LoginFlowRoute.Reported -> {} /* 화면 이동 없이 팝업만 동작 */
        }
    }

    private fun navigateToMain() {
        phoneVerificationSessionManager.setPhoneChecked(false)
        socketEventBus.connect()
        navigator.navigateTo(
            key = HomeScreenKey,
            popUpTo = LoginScreenKey,
            inclusive = true,
        )
    }

    private fun navigateToTerms() {
        navigator.navigateTo(TermsScreenKey)
    }
}
