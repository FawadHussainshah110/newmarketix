package com.example.marketix.di.module

import android.app.Application
import androidx.room.Room
import com.example.marketix.data.source.local.AppDatabase
import com.example.marketix.data.source.local.dao.DummyUserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class DatabaseModule {

    @Provides
    @Singleton
    internal open fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            AppDatabase.DB_NAME
        ).allowMainThreadQueries().build()
    }

    @Provides
    internal open fun provideUserDao(appDatabase: AppDatabase): DummyUserDao {
        return appDatabase.userDao
    }


}