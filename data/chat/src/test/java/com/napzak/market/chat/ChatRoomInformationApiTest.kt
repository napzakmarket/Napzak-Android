package com.napzak.market.chat

import com.napzak.market.chat.datasource.ChatRoomDataSource
import com.napzak.market.chat.repositoryimpl.ChatRoomRepositoryImpl
import com.napzak.market.chat.service.ChatRoomService
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ChatRoomInformationApiTest : ApiAbstract<ChatRoomService>() {
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
    fun `fetch ChatRoomInformationResponse From Service`() = runTest {
        // given
        enqueueResponse("ChatRoomInformation.json")

        // when
        val response = service.getProductChatInformation(roomId = 5)
        mockWebServer.takeRequest()

        // then
        assertEquals(5, response.data.roomId)

        assertEquals("BUY", response.data.productInfo.tradeType)
        assertEquals("슈가슈가룬 쇼콜라 브라이스 인형", response.data.productInfo.title)

        assertEquals(2, response.data.storeInfo.storeId)
        assertEquals("납자기", response.data.storeInfo.nickname)
    }

    @Test
    fun `fetch ChatRoomInformationResponse From DataSource`() = runTest {
        // given
        enqueueResponse("ChatRoomInformation.json")

        // when
        val response = ChatRoomDataSource(service).getChatRoomInformation(productId = 5)
        mockWebServer.takeRequest()

        // then
        assertEquals(5, response.data.roomId)

        assertEquals("BUY", response.data.productInfo.tradeType)
        assertEquals("슈가슈가룬 쇼콜라 브라이스 인형", response.data.productInfo.title)

        assertEquals(2, response.data.storeInfo.storeId)
        assertEquals("납자기", response.data.storeInfo.nickname)
    }

    @Test
    fun `fetch ChatRoomInformationResponse From Repository`() = runTest {
        // given
        enqueueResponse("ChatRoomInformation.json")
        val dataSource = ChatRoomDataSource(service)

        // when
        val result = ChatRoomRepositoryImpl(dataSource)
            .getChatRoomInformation(productId = 5)
            .getOrNull()
        mockWebServer.takeRequest()


        // then
        assert(result?.roomId != null) { "Repository 매핑 결과가 널 입니다." }
        if (result?.roomId != null) {
            with(result) {
                assertEquals(5L, roomId)

                assertEquals("BUY", productBrief.tradeType)
                assertEquals("슈가슈가룬 쇼콜라 브라이스 인형", productBrief.title)

                assertEquals(2, storeBrief.storeId)
                assertEquals("납자기", storeBrief.nickname)
            }
        }
    }
}
