package com.napzak.market.update.usecase

import com.napzak.market.update.repository.RemoteConfigRepository
import net.swiftzer.semver.SemVer
import javax.inject.Inject

class CheckAppVersionUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
) {
    suspend operator fun invoke(
        appVersion: String,
    ): Boolean {
        val versionFromRemoteConfig = remoteConfigRepository.getFirebaseRemoteConfig()
        val latestVersion = SemVer.parse(versionFromRemoteConfig)
        val currentVersion = SemVer.parse(appVersion)

        return when {
            currentVersion.major < latestVersion.major -> true
            currentVersion.major == latestVersion.major && currentVersion.minor < latestVersion.minor -> true
            else -> false
        }
    }
}
