package com.napzak.market.update.usecase

import com.napzak.market.update.repository.AppVersionRepository
import javax.inject.Inject

class CheckAppVersionUseCase @Inject constructor(
    private val appVersionRepository: AppVersionRepository,
) {
    suspend operator fun invoke(): Boolean {
        var appVersion = appVersionRepository.getAppVersion()
        val latestVersion = appVersionRepository.getFirebaseVersion()

        if (appVersion == null || appVersion != latestVersion) {
            appVersionRepository.setAppVersion(latestVersion!!)
            appVersion = appVersionRepository.getAppVersion()
        }

        return appVersion != latestVersion
    }
}
