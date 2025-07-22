package com.napzak.market.chat

import com.napzak.market.chat.datasource.ChatDataSource
import com.napzak.market.chat.repositoryimpl.ChatRepositoryImpl
import com.napzak.market.chat.service.ChatService
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ChatRoomListApiTest : ApiAbstract<ChatService>() {
    private lateinit var service: ChatService

    @Before
    fun initService() {
        super.createMockServer()
        service = createService(ChatService::class.java)
    }

    @After
    fun tearDown() {
        super.stopServer()
    }

    @Test
    fun `fetch ChatRoomListResponse From Service`() = runTest {
        // given
        enqueueResponse("ChatRoomList.json")

        // when
        val response = service.getChatRooms(null)
        mockWebServer.takeRequest()

        // then
        assertEquals(6, response.data.chatRooms[0].roomId)
        assertEquals("납자기", response.data.chatRooms[1].opponentNickname)
    }

    @Test
    fun `fetch chatRooms from repository`() = runTest {
        // given
        enqueueResponse("ChatRoomList.json")
        val datasource = ChatDataSource(service)

        // when
        val result = ChatRepositoryImpl(datasource)
            .getChatRooms()
            .getOrNull()
        mockWebServer.takeRequest()

        // then
        if (result != null) {
            assertEquals(6, result[0].roomId)
            assertEquals("납자기", result[1].storeNickname)
        }
    }
}