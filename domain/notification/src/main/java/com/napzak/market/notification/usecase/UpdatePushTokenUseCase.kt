package com.napzak.market.notification.usecase

import com.napzak.market.notification.model.EntireNotificationSettings
import com.napzak.market.notification.repository.NotificationRepository
import javax.inject.Inject

class UpdatePushTokenUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke(
        pushToken: String,
        allowMessage: Boolean,
        isEnabled: Boolean,
    ): Result<Unit> {
        val request = EntireNotificationSettings(
            deviceToken = pushToken,
            isEnabled = isEnabled,
            allowMessage = allowMessage
        )
        return notificationRepository.updatePushToken(request)
    }
}
