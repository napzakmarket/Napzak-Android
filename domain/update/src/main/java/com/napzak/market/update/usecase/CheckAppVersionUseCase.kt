package com.napzak.market.update.usecase

import com.napzak.market.update.repository.AppVersionRepository
import javax.inject.Inject

class CheckAppVersionUseCase @Inject constructor(
    private val appVersionRepository: AppVersionRepository,
) {
    suspend operator fun invoke(): Boolean =
        appVersionRepository.getFirebaseVersion()
            ?.takeIf { it != appVersionRepository.getAppVersion() }
            ?.let {
                appVersionRepository.setAppVersion(it)
                true
            } == true
}
