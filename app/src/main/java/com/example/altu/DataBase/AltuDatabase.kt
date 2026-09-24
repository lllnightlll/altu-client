package com.example.altu.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.altu.DataBase.dao.MessageQueueDao
import com.example.altu.DataBase.dao.UserDao
import com.example.altu.DataBase.entity.QueuedMessageEntity
import com.example.altu.DataBase.entity.UserEntity

@Database(
    entities = [UserEntity::class, QueuedMessageEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class AltuDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun messageQueueDao(): MessageQueueDao

    companion object {
        private const val NAME = "altu.db"

        @Volatile
        private var instance: AltuDatabase? = null

        fun get(context: Context): AltuDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AltuDatabase::class.java,
                    NAME,
                ).fallbackToDestructiveMigration(dropAllTables = true).build().also { instance = it }
            }
        }
    }
}
