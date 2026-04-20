package com.napzak.market.local.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.napzak.market.local.DbAbstract
import com.napzak.market.local.room.dao.ChatRemoteKeyDao
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.napzak.market.local.dummy.DummyChatRemoteKeyFactory as DummyFactory

@RunWith(AndroidJUnit4::class)
class ChatRemoteKeyDaoTest : DbAbstract() {
    lateinit var dao: ChatRemoteKeyDao

    @Before
    fun setUp() {
        createDb()
        dao = db.chatRemoteKeyDao()
    }

    @After
    fun tearDown() {
        closeDb()
    }

    @Test
    fun check_if_new_key_is_inserted() = runTest {
        // given
        val targetId = 1L
        val expected = DummyFactory.createEntity(targetId)

        // when
        dao.upsertKey(expected)
        val actual = dao.getRemoteKey(targetId)

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun check_if_key_is_deleted() = runTest {
        // given
        val targetId = 1L
        val entity = DummyFactory.createEntity(targetId)

        // when
        dao.upsertKey(entity)
        dao.deleteRemoteKey(entity.roomId)
        val actual = dao.getRemoteKey(targetId)

        // then
        assertEquals(null, actual)
    }
}