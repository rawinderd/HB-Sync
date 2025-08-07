package com.hook2book.hbsync.Model.NewTag

import com.google.gson.annotations.SerializedName
import com.hook2book.hbsync.Model.Categories.LocalCategories
import com.hook2book.hbsync.RoomDatabase.CategoryEntity
import java.io.Serializable

class CategoriesForListing2(
    @SerializedName("categoryDetail") var categoryDetail: CategoryEntity,
    @SerializedName("IsParent") var IsParent: String,
    @SerializedName("parentId") var parentId: Int?,
    @SerializedName("parentName") var parentName: String?
) : Serializable