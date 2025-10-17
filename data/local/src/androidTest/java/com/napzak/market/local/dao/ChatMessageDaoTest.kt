package com.napzak.market.local.dao

import androidx.paging.PagingSource.LoadParams.Refresh
import androidx.paging.PagingSource.LoadResult.Page
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.napzak.market.local.DbAbstract
import com.napzak.market.local.room.dao.ChatMessageDao
import com.napzak.market.local.room.type.ChatStatusType
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.napzak.market.local.dummy.DummyChatMessageEntityFactory as DummyFactory

@RunWith(AndroidJUnit4::class)
class ChatMessageDaoTest : DbAbstract() {
    lateinit var dao: ChatMessageDao

    @Before
    fun setUp() {
        createDb()
        dao = db.chatMessageDao()
    }

    @After
    fun tearDown() {
        closeDb()
    }

    @Test
    fun check_if_inserted_entity_is_accessed_correctly() = runTest {
        //given
        val targetId = 1L
        val expected = DummyFactory.createEntity(targetId)
        val entities = DummyFactory.createEntities(10)

        //when
        dao.insertChatMessages(entities)
        val pagingSource = dao.getChatMessages(targetId)
        val loadedData = (pagingSource.load(
            Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        ) as Page).data.firstOrNull() ?: return@runTest

        //then
        assertEquals(expected, loadedData)
    }

    @Test
    fun check_if_latest_message_is_accessed_correctly() = runTest {
        //given
        val targetId = 1L
        val expected = DummyFactory.createEntity(targetId)
        val entities = DummyFactory.createEntities(10)

        // when
        dao.insertChatMessages(entities)
        val lastMessage = async {
            dao.getLatestMessageAsFlow(targetId)?.firstOrNull()
        }.await()

        // then
        assertEquals(expected, lastMessage)
    }

    @Test
    fun check_if_messageId_and_status_are_updated_by_uuid() = runTest {
        // given
        val targetId = 1L
        val expected = DummyFactory.createEntity(targetId, status = ChatStatusType.RECEIVED)
        val entity = DummyFactory.createEntity(id = targetId, messageId = null)

        // when
        dao.insertChatMessages(listOf(entity))
        dao.updateMessageIdAndStatusByUuid(expected)
        val lastMessage = async {
            dao.getLatestMessageAsFlow(targetId)?.firstOrNull()
        }.await()

        // then
        assertEquals(expected, lastMessage)
    }

    @Test
    fun check_if_message_is_mark_as_read() = runTest {
        // given
        val targetId = 1L
        val expected = DummyFactory.createEntity(targetId)
        val roomId = expected.roomId ?: return@runTest
        val isMessageOwner = expected.isMessageOwner

        // when
        dao.insertChatMessages(listOf(expected))
        dao.markMessagesAsRead(roomId, isMessageOwner)
        val lastMessage = async {
            dao.getLatestMessageAsFlow(targetId)?.firstOrNull()
        }.await()

        // then
        assertEquals(true, lastMessage?.isRead)
    }

}