package com.napzak.market.local.room

import android.content.Context
import androidx.room.Room
import com.napzak.market.local.room.dao.ChatMessageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    /**
     * Database를 확장한다면, 확장에 대한 내용을 Migration 객체로 만들어 데이터베이스 스키마 변경에 대한 지침을 남겨야 합니다.
     *
     * ```
     * private val MIGRATION_1_2 =
     *     object : Migration(1, 2) {
     *          override fun migrate(db: SupportSQLiteDatabase) {
     *              db.execSQL(
     *                  """
     *                         CREATE TABLE IF NOT EXISTS ChatMessage (
     *                            id INTEGER PRIMARY KEY NOT NULL,
     *                            text TEXT NOT NULL DEFAULT '',
     *                            createdAt TEXT NOT NULL DEFAULT '',
     *                         )
     *                  """.trimIndent()
     *              )
     *          }
     *    }
     * ```
     *
     * 생성한 Migration 객체는 `databaseBuilder()`에 `addMigrations()`을 체이닝하고 메서드 인자로 추가합니다.
     * ```
     * @Singleton
     *     @Provides
     *     fun provideNapzakDatabase(
     *         @ApplicationContext context: Context
     *     ): NapzakDatabase = Room
     *          .databaseBuilder(
     *              context = context,
     *              klass =NapzakDatabase::class.java,
     *              name = "napzak-database"
     *          )
     *          .addMigrations(MIGRATION_1_2)
     *          .build()
     * ```
     */
    @Singleton
    @Provides
    fun provideNapzakDatabase(
        @ApplicationContext context: Context
    ): NapzakDatabase = Room
        .databaseBuilder(
            context = context,
            klass = NapzakDatabase::class.java,
            name = "napzak-database"
        )
        .build()

    @Singleton
    @Provides
    fun provideChatMessageDao(
        napzakDatabase: NapzakDatabase
    ): ChatMessageDao = napzakDatabase.chatMessageDao()
}