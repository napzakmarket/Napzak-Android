package com.napzak.market.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.napzak.market.local.room.NapzakDatabase

abstract class DbAbstract {
    lateinit var db: NapzakDatabase

    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, NapzakDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    fun closeDb() {
        db.close()
    }
}