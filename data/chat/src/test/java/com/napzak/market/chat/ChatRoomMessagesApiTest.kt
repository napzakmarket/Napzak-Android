package com.napzak.market.chat

import com.napzak.market.chat.dto.MessageMetadata
import com.napzak.market.chat.service.ChatRoomService
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class ChatRoomMessagesApiTest : ApiAbstract<ChatRoomService>() {
    private lateinit var service: ChatRoomService

    @Before
    fun initService() {
        super.createMockServer()
        service = createService(ChatRoomService::class.java)
    }

    @After
    fun tearDown() {
        super.stopServer()
    }

    @Test
    fun `fetch ChatRoomMessagesResponse From Service`() = runTest {
        // given
        enqueueResponse("ChatRoomMessages.json")

        // when
        val response = service.getChatRoomMessages(roomId = 5, cursor = null, size = null)
        mockWebServer.takeRequest()

        // then
        assert(response.data.messages[0].metadata is MessageMetadata.Image)
        assert(response.data.messages[1].metadata is MessageMetadata.Product)
        assert(response.data.messages[2].metadata == null) // Text는 metadata가 null
        assert(response.data.messages[3].metadata == null) // Text는 metadata가 null
        assert(response.data.messages[4].metadata is MessageMetadata.Date)
    }
}