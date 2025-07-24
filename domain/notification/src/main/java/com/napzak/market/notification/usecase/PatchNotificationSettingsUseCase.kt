package com.napzak.market.notification.usecase

import com.napzak.market.notification.repository.NotificationRepository
import javax.inject.Inject

class PatchNotificationSettingsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke(pushToken: String, allowMessage: Boolean): Result<Unit> {
        return notificationRepository.patchNotificationSettings(pushToken, allowMessage)
    }
}
