package com.napzak.market.chat

import com.napzak.market.chat.datasource.ChatRoomDataSource
import com.napzak.market.chat.repositoryimpl.ChatRoomRepositoryImpl
import com.napzak.market.chat.service.ChatRoomService
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotEquals

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
        val response = service.getProductChatInformation(productId = 5, roomId = null)
        mockWebServer.takeRequest()

        // then
        assertEquals(6L, response.data.roomId)

        assertEquals("SELL", response.data.productInfo.tradeType)
        assertEquals("레고 아키텍처 21061 파리 노트르담 [레고공식]", response.data.productInfo.title)

        assertEquals(3, response.data.storeInfo.storeId)
        assertEquals("납작한 시나모롤", response.data.storeInfo.nickname)
    }

    @Test
    fun `fetch ChatRoomInformationResponse From DataSource`() = runTest {
        // given
        enqueueResponse("ChatRoomInformation.json")

        // when
        val response =
            ChatRoomDataSource(service).getChatRoomInformation(productId = 3, roomId = null)
        mockWebServer.takeRequest()

        // then
        assertEquals(6L, response.data.roomId)

        assertEquals("SELL", response.data.productInfo.tradeType)
        assertEquals("레고 아키텍처 21061 파리 노트르담 [레고공식]", response.data.productInfo.title)

        assertEquals(3, response.data.storeInfo.storeId)
        assertEquals("납작한 시나모롤", response.data.storeInfo.nickname)
    }

    @Test
    fun `fetch ChatRoomInformationResponse From Repository`() = runTest {
        // given
        enqueueResponse("ChatRoomInformation.json")
        val dataSource = ChatRoomDataSource(service)

        // when
        val result = ChatRoomRepositoryImpl(dataSource)
            .getChatRoomInformation(productId = 3, roomId = 6L)
            .getOrNull()
        mockWebServer.takeRequest()


        // then
        assert(result?.roomId != null) { "Repository 매핑 결과가 널 입니다." }
        if (result?.roomId != null) {
            with(result) {
                assertEquals(6L, roomId)

                assertEquals("SELL", productBrief.tradeType)
                assertEquals("레고 아키텍처 21061 파리 노트르담 [레고공식]", productBrief.title)

                assertEquals(3, storeBrief.storeId)
                assertNotEquals("납자기", storeBrief.nickname)
            }
        }
    }
}
