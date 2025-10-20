package com.napzak.market.chat.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.napzak.market.chat.datasource.ChatRoomDataSource
import com.napzak.market.chat.datasource.ChatRoomMessageMediator
import com.napzak.market.local.room.NapzakDatabase
import com.napzak.market.local.room.entity.ChatMessageWithProduct
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalPagingApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ChatRoomMessageMediatorTest {
    private val roomId = 1L
    private val dataSource = Mockito.mock(ChatRoomDataSource::class.java)
    private lateinit var database: NapzakDatabase
    private lateinit var mediator: ChatRoomMessageMediator

    @Before
    fun setUp() {
        initDatabase()
        initMediator()
    }

    fun initMediator() {
        mediator = ChatRoomMessageMediator(
            roomId = roomId,
            chatRoomDataSource = dataSource,
            database = database,
            chatMessageDao = database.chatMessageDao(),
            chatProductDao = database.chatProductDao(),
            remoteKeyDao = database.chatRemoteKeyDao(),
        )
    }

    fun initDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            NapzakDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `ChatRoomMessageMediator is refreshed and returns some notices`() = runTest {
        // given
        val pageSize = ChatRoomMessageMediator.PAGE_SIZE
        val mockResponse = ChatRoomMessageTestUtil.mockResponse(cursor = "cursor")
        Mockito.`when`(
            dataSource.getChatRoomMessages(
                roomId = roomId,
                cursor = null,
                size = pageSize,
            )
        ).thenReturn(mockResponse)

        // when
        val pagingState = PagingState<Int, ChatMessageWithProduct>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize),
            leadingPlaceholderCount = pageSize,
        )
        val result = mediator.load(loadType = LoadType.REFRESH, state = pagingState)

        // then
        assert(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `ChatRoomMessageMediator is appended and reaches the end`() = runTest {
        // given
        val pageSize = ChatRoomMessageMediator.PAGE_SIZE
        val mockResponse = ChatRoomMessageTestUtil.mockResponse()
        Mockito.`when`(
            dataSource.getChatRoomMessages(
                roomId = roomId,
                cursor = null,
                size = pageSize,
            )
        ).thenReturn(mockResponse)

        // when
        val pagingState = PagingState<Int, ChatMessageWithProduct>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize),
            leadingPlaceholderCount = pageSize,
        )
        val result = mediator.load(loadType = LoadType.REFRESH, state = pagingState)

        // then
        assert(result is RemoteMediator.MediatorResult.Success)
        assert((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }
}