package com.napzak.market.store.mapper

import com.napzak.market.store.dto.response.CodeVerificationResponse
import com.napzak.market.store.dto.response.GenreDto
import com.napzak.market.store.dto.response.KakaoLoginResponse
import com.napzak.market.store.dto.response.PhoneCodeResponse
import com.napzak.market.store.dto.response.PhoneVerificationStatusResponse
import com.napzak.market.store.dto.response.StoreResponse
import com.napzak.market.store.dto.response.TermsResponse
import com.napzak.market.store.dto.response.WithdrawResponse
import com.napzak.market.store.model.Genre
import com.napzak.market.store.model.KakaoLogin
import com.napzak.market.store.model.PhoneCodeVerificationResult
import com.napzak.market.store.model.StoreInfo
import com.napzak.market.store.model.Terms
import com.napzak.market.store.model.TermsAgreement
import com.napzak.market.store.model.UserRole
import com.napzak.market.store.model.UserWithdrawal

fun GenreDto.toDomain(): Genre {
    return Genre(
        id = this.genreId,
        name = this.genreName,
    )
}

fun WithdrawResponse.toDomain(): UserWithdrawal {
    return UserWithdrawal(
        storeId = this.storeId,
        title = this.withdrawTitle,
        description = this.withdrawDescription,
    )
}

fun StoreResponse.toDomain(): StoreInfo = StoreInfo(
    storeId = storeId,
    nickname = storeNickName,
    photoUrl = storePhoto,
    salesCount = totalSellCount,
    purchaseCount = totalBuyCount,
    serviceLink = serviceLink,
)

fun TermsResponse.toDomain(): TermsAgreement = TermsAgreement(
    bundleId = bundleId,
    termList = termList.map { dto ->
        Terms(
            termsId = dto.termsId,
            termsTitle = dto.termsTitle,
            termsUrl = dto.termsUrl,
            isRequired = dto.isRequired,
        )
    }
)

fun KakaoLoginResponse.toDomain(): KakaoLogin {
    return KakaoLogin(
        accessToken = accessToken,
        refreshToken = refreshToken,
        nickname = nickname,
        role = UserRole.from(role).toString()
    )
}

fun PhoneVerificationStatusResponse.toDomain(): Boolean {
    return this.isPhoneVerified
}

fun PhoneCodeResponse.toDomain(): Int {
    return this.remainingRequestCount
}

fun CodeVerificationResponse.toDomain(): PhoneCodeVerificationResult {
    return PhoneCodeVerificationResult(
        isPhoneVerified = isPhoneVerified,
        remainingRequestCount = remainingRequestCount,
    )
}
