package com.napzak.market.chat

import com.napzak.market.chat.service.ChatService
import com.napzak.market.local.room.dao.ChatRoomDao
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ChatRoomListApiTest : ApiAbstract<ChatService>() {
    private lateinit var service: ChatService
    private val chatRoomDao = Mockito.mock(ChatRoomDao::class.java)

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
}