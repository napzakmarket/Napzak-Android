package com.napzak.market.dummy.service

import com.napzak.market.dummy.dto.GetUserListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DummyService {

    @GET("/api/users")
    suspend fun getUserList(@Query("page") page: Int): GetUserListResponse
}