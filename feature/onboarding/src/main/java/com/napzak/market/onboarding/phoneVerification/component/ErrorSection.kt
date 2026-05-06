package com.napzak.market.onboarding.phoneVerification.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.napzak.market.feature.onboarding.R
import com.napzak.market.onboarding.phoneVerification.model.PhoneVerificationError

@Composable
fun ErrorSection(
    modifier: Modifier = Modifier,
    error: PhoneVerificationError,
) {
    val painter = when (error) {
        PhoneVerificationError.InvalidName -> painterResource(R.drawable.img_error_name)
        PhoneVerificationError.InvalidPhoneNumber -> painterResource(R.drawable.img_error_phone)
        PhoneVerificationError.PhoneAlreadyRegistered -> painterResource(R.drawable.img_error_phone_already_registered)
        PhoneVerificationError.PhoneNotAllowed -> painterResource(R.drawable.img_error_phone_not_allowed)
        PhoneVerificationError.NetworkError -> painterResource(R.drawable.img_error_network)
        PhoneVerificationError.VerificationRequestLimitExceeded -> painterResource(R.drawable.img_error_verification_request_limit_exceeded)
        PhoneVerificationError.VerificationTimeExpired -> painterResource(R.drawable.img_error_verification_time_expired)
        PhoneVerificationError.InvalidVerificationCode -> painterResource(R.drawable.img_error_verification_code)
        PhoneVerificationError.VerificationCodeAttemptsExceeded -> painterResource(R.drawable.img_error_verification_code_attempts_exceeded)
        else -> return
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painter,
            contentDescription = null,
        )
    }
}
