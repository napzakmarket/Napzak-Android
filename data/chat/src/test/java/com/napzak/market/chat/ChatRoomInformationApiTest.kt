package com.napzak.market.chat

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.napzak.market.chat.datasource.ChatRoomDataSource
import com.napzak.market.chat.repository.ChatRoomRepository
import com.napzak.market.chat.repositoryimpl.ChatRoomRepositoryImpl
import com.napzak.market.chat.service.ChatRoomService
import com.napzak.market.local.room.NapzakDatabase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ChatRoomInformationApiTest : ApiAbstract<ChatRoomService>() {
    private lateinit var service: ChatRoomService
    private lateinit var database: NapzakDatabase
    private lateinit var repository: ChatRoomRepository

    @Before
    fun setUp() {
        initService()
        initDatabase()
        initRepository()
    }

    fun initService() {
        super.createMockServer()
        service = createService(ChatRoomService::class.java)
    }

    fun initDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            NapzakDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    fun initRepository() {
        val datasource = ChatRoomDataSource(service)
        repository = ChatRoomRepositoryImpl(
            chatRoomDataSource = datasource,
            napzakDatabase = database,
            chatMessageDao = database.chatMessageDao(),
            chatProductDao = database.chatProductDao(),
            chatRemoteKeyDao = database.chatRemoteKeyDao(),
        )
    }

    @After
    fun tearDown() {
        super.stopServer()
        database.close()
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

        // when
        val result = repository
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
