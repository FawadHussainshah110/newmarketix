package com.example.marketix.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.marketix.data.source.local.dao.DummyUserDao
import com.example.marketix.domain.model.DummyUser

/**
 * To manage data items that can be accessed, updated
 * & maintain relationships between them
 *
 * @Created by Abdullah
 */
@Database(entities = [DummyUser::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val userDao: DummyUserDao

    companion object {
        const val DB_NAME = "GoSayHelloBusiness.db"
    }
}
