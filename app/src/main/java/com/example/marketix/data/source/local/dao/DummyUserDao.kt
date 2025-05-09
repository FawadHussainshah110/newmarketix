package com.example.marketix.data.source.local.dao

import androidx.room.*
import com.example.marketix.domain.model.DummyUser

/**
 * it provides access to [Photo] underlying database
 * */
@Dao
interface DummyUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: DummyUser): Long

    @Query("SELECT * FROM dummy_user")
    fun loadAll(): DummyUser

    @Delete
    fun delete(photo: DummyUser)

    @Query("DELETE FROM dummy_user")
    fun deleteAll()

    @Query("SELECT * FROM dummy_user where id = :userId")
    fun loadOneByPhotoId(userId: Long): DummyUser?

    @Query("SELECT * FROM dummy_user where name = :name")
    fun loadOneByPhotoTitle(name: String): DummyUser?

    @Update
    fun update(user: DummyUser)

}