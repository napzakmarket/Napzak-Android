package com.napzak.market.local.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.napzak.market.local.DbAbstract
import com.napzak.market.local.room.dao.ChatRoomDao
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.napzak.market.local.dummy.DummyChatRoomFactory as DummyFactory

@RunWith(AndroidJUnit4::class)
class ChatRoomDaoTest : DbAbstract() {
    lateinit var dao: ChatRoomDao

    @Before
    fun setUp() {
        createDb()
        dao = db.chatRoomDao()
    }

    @After
    fun tearDown() {
        closeDb()
    }

    @Test
    fun check_if_new_room_is_inserted() = runTest {
        // given
        val targetId = 1L
        val expected = DummyFactory.createEntity(targetId)

        // when
        dao.safeUpsertChatRooms(listOf(expected), false)
        val actual = dao.getChatRoom(targetId)

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun check_if_room_is_updated() = runTest {
        // given
        val targetId = 1L
        val entity = DummyFactory.createEntity(targetId)

        // when
        dao.safeUpsertChatRooms(listOf(entity), false)
        dao.upsertChatRoom(entity.copy(isChatBlocked = true))
        val actual = dao.getChatRoom(targetId)

        // then
        assertEquals(true, actual?.isChatBlocked)
    }

    @Test
    fun check_if_room_is_deleted() = runTest {
        // given
        val targetId = 1L
        val entity = DummyFactory.createEntity(targetId)

        // when
        dao.upsertChatRoom(entity)
        dao.deleteChatRoom(targetId)
        val actual = dao.getChatRoom(targetId)

        // then
        assertEquals(null, actual)
    }
}