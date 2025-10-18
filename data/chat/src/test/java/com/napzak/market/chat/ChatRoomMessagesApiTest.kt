package com.napzak.market.chat

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.napzak.market.chat.datasource.ChatRoomDataSource
import com.napzak.market.chat.dto.ChatMessageMetadata
import com.napzak.market.chat.model.ReceiveMessage
import com.napzak.market.chat.repository.ChatRoomRepository
import com.napzak.market.chat.repositoryimpl.ChatRoomRepositoryImpl
import com.napzak.market.chat.service.ChatRoomService
import com.napzak.market.local.room.NapzakDatabase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ChatRoomMessagesApiTest : ApiAbstract<ChatRoomService>() {
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
    fun `fetch ChatRoomMessagesResponse From Service`() = runTest {
        // given
        enqueueResponse("ChatRoomMessages.json")

        // when
        val response = service.getChatRoomMessages(roomId = 5, cursor = null, size = null)
        mockWebServer.takeRequest()

        // then
        assert(response.data.messages[0].metadata is ChatMessageMetadata.Image)
        assert(response.data.messages[1].metadata is ChatMessageMetadata.Product)
        assert(response.data.messages[2].metadata == null) // Text는 metadata가 null
        assert(response.data.messages[3].metadata == null) // Text는 metadata가 null
        assert(response.data.messages[4].metadata is ChatMessageMetadata.Date)
    }

    @Test
    fun `fetch chatItems from repository`() = runTest {
        // given
        enqueueResponse("ChatRoomMessages.json")

        // when
        val result = repository
            .getChatRoomMessages(roomId = 5)
            .getOrNull()
        mockWebServer.takeRequest()

        // then
        if (result != null) {
            assertEquals(ReceiveMessage.Image::class, result[0]::class)
            assertEquals(ReceiveMessage.Product::class, result[1]::class)
            assertEquals(ReceiveMessage.Text::class, result[2]::class)
            assertEquals(ReceiveMessage.Text::class, result[3]::class)
            assertEquals(ReceiveMessage.Date::class, result[4]::class)
        }
    }
}