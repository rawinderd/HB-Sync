package com.hook2book.hbsync.RoomDatabase


import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


class CategoriesMainForRoom
    (
    @SerializedName("count") val count: Int,
    @SerializedName("description") val description: String,
    @SerializedName("id") val id: Int,
    @SerializedName("CategoryName") val CategoryName: String,
    @SerializedName("CategoryParent") val CategoryParent: Int,
)