package com.napzak.market.onboarding.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.napzak.market.event.SocketEventBus
import com.napzak.market.navigation.AppNavigator
import com.napzak.market.navigation.EntryProviderBuilder
import com.napzak.market.navigation.keys.GenreScreenKey
import com.napzak.market.navigation.keys.HomeScreenKey
import com.napzak.market.navigation.keys.LoginScreenKey
import com.napzak.market.navigation.keys.NicknameScreenKey
import com.napzak.market.navigation.keys.PhoneVerificationScreenKey
import com.napzak.market.navigation.keys.ScreenKey
import com.napzak.market.navigation.keys.TermsScreenKey
import com.napzak.market.onboarding.genre.GenreRoute
import com.napzak.market.onboarding.nickname.NicknameRoute
import com.napzak.market.onboarding.phoneVerification.PhoneVerificationRoute
import com.napzak.market.onboarding.termsAgreement.TermsAgreementRoute
import javax.inject.Inject

class OnboardingEntryProvider @Inject constructor(
    private val navigator: AppNavigator,
    private val socketEventBus: SocketEventBus,
) : EntryProviderBuilder {
    override fun EntryProviderScope<ScreenKey>.provide() {
        entry<TermsScreenKey> {
            TermsAgreementRoute(
                onBackClick = navigator::pop,
                onNextClick = ::navigateToPhoneVerification,
            )
        }

        entry<PhoneVerificationScreenKey> { key ->
            PhoneVerificationRoute(
                isOnboarding = key.isOnboarding,
                onBackClick = navigator::pop,
                onNextClick = ::navigateToNickname,
            )
        }

        entry<NicknameScreenKey> {
            NicknameRoute(
                onBackClick = navigator::pop,
                onNextClick = ::navigateToGenre,
            )
        }

        entry<GenreScreenKey> {
            GenreRoute(
                onBackClick = navigator::pop,
                onDoneClick = ::navigateToHome,
                onSkipClick = ::navigateToHome,
            )
        }
    }

    private fun navigateToPhoneVerification() {
        navigator.navigateTo(PhoneVerificationScreenKey(true))
    }

    private fun navigateToNickname() {
        navigator.navigateTo(NicknameScreenKey)
    }

    private fun navigateToGenre() {
        navigator.navigateTo(GenreScreenKey)
    }

    private fun navigateToHome() {
        socketEventBus.connect()
        navigator.navigateTo(
            key = HomeScreenKey,
            popUpTo = LoginScreenKey,
            inclusive = true,
        )
    }
}
