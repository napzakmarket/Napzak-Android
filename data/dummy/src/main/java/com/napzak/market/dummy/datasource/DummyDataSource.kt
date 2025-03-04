package com.napzak.market.dummy.datasource

import com.napzak.market.dummy.dto.GetUserListResponse
import com.napzak.market.dummy.service.DummyService
import javax.inject.Inject

class DummyDataSource @Inject constructor(
    private val dummyService: DummyService
) {
    suspend fun getDummyUserList(page: Int): GetUserListResponse {
        return dummyService.getUserList(page)
    }
}