package com.napzak.market.notification.repository

interface FirebaseRepository {
    suspend fun getPushTokenFromFirebase()
    suspend fun deletePushTokenFromFirebase()
}
