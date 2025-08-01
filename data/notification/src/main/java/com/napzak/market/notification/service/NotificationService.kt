package com.napzak.market.notification.service

import com.napzak.market.notification.dto.NotificationSettingsRequest
import com.napzak.market.notification.dto.NotificationSettingsResponse
import com.napzak.market.notification.dto.UpdatePushTokenRequest
import com.napzak.market.remote.model.BaseResponse
import com.napzak.market.remote.model.EmptyDataResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationService {
    @POST("push-tokens")
    suspend fun updatePushToken(
        @Body request: UpdatePushTokenRequest,
    ): EmptyDataResponse

    @DELETE("push-tokens/{deviceToken}")
    suspend fun deletePushToken(
        @Path("deviceToken") deviceToken: String,
    ): EmptyDataResponse

    @GET("push-tokens/{deviceToken}/settings")
    suspend fun getNotificationSettings(
        @Path("deviceToken") deviceToken: String,
    ): BaseResponse<NotificationSettingsResponse>

    @PATCH("push-tokens/{deviceToken}/settings")
    suspend fun patchNotificationSettings(
        @Path("deviceToken") deviceToken: String,
        @Body request: NotificationSettingsRequest,
    ): EmptyDataResponse
}
