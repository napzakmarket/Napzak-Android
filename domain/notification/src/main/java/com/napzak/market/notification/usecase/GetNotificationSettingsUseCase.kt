package com.napzak.market.notification.usecase

import com.napzak.market.notification.model.NotificationSettings
import com.napzak.market.notification.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationSettingsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke(pushToken: String): Result<NotificationSettings> {
        return notificationRepository.getNotificationSettings(pushToken)
    }
}
