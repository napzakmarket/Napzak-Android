package com.napzak.market.dummy.repository

import com.napzak.market.dummy.model.DummyUser

interface DummyRepository {
    suspend fun fetchDummyUserList(page: Int): Result<List<DummyUser>>
}