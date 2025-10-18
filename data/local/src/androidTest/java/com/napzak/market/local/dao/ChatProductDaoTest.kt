package com.napzak.market.local.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.napzak.market.local.DbAbstract
import com.napzak.market.local.room.dao.ChatProductDao
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.napzak.market.local.dummy.DummyChatProductFactory as DummyFactory

@RunWith(AndroidJUnit4::class)
class ChatProductDaoTest : DbAbstract() {
    lateinit var dao: ChatProductDao

    @Before
    fun setUp() {
        createDb()
        dao = db.chatProductDao()
    }

    @After
    fun tearDown() {
        closeDb()
    }

    @Test
    fun check_if_new_key_is_inserted() = runTest {
        // given
        val targetId = 1L
        val expected = DummyFactory.createEntity(targetId)

        // when
        dao.upsertProduct(expected)
        val actual = dao.getProduct(targetId)

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun check_if_product_is_updated() = runTest {
        // given
        val targetId = 1L
        val newTitle = "new title"
        val entity = DummyFactory.createEntity(targetId)

        // when
        dao.upsertProduct(entity)
        dao.upsertProduct(entity.copy(title = newTitle))
        val actual = dao.getProduct(targetId)

        // then
        assertEquals(newTitle, actual?.title)
    }

    @Test
    fun check_if_product_is_deleted() = runTest {
        // given
        val targetId = 1L
        val entity = DummyFactory.createEntity(targetId)

        // when
        dao.upsertProduct(entity)
        dao.deleteProduct(targetId)
        val actual = dao.getProduct(targetId)

        // then
        assertEquals(null, actual)
    }
}