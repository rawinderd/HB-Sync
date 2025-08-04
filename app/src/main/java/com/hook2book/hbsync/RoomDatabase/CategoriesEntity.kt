package com.hook2book.hbsync.RoomDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "categories")
data class CategoriesEntity(

    @ColumnInfo(name = "count") val count: Int,
    @ColumnInfo(name = "description") val description: String,
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "CategoryName") val name: String,
    @ColumnInfo(name = "CategoryParent") val parent: Int,
)