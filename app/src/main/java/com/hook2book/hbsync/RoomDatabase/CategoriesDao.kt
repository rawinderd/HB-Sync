package com.hook2book.hbsync.RoomDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hook2book.hbsync.Model.Categories.CategoriesMain

@Dao
interface CategoriesDao {

    @Query("SELECT * FROM categories")
    suspend fun getAll(): List<CategoriesMainForRoom>

    @Insert
    suspend fun insertAll(category: List<CategoriesMainForRoom>)

    @Delete
    suspend fun delete(category: CategoriesMainForRoom)
}