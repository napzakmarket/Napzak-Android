package com.napzak.market.navigation.keys

import kotlinx.serialization.Serializable

@Serializable
data object TermsScreenKey : ScreenKey

@Serializable
data class PhoneVerificationScreenKey(
    val isOnboarding: Boolean,
) : ScreenKey

@Serializable
data object NicknameScreenKey : ScreenKey

@Serializable
data object GenreScreenKey : ScreenKey
