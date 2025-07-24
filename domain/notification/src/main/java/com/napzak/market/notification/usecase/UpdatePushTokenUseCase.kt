package com.napzak.market.notification.usecase

import com.napzak.market.notification.repository.NotificationRepository
import javax.inject.Inject

class UpdatePushTokenUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke(pushToken: String): Result<Unit> {
        return notificationRepository.updatePushToken(pushToken)
    }
}
