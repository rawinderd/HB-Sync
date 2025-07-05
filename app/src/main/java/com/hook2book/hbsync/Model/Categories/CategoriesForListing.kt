package com.hook2book.hbsync.Model.NewTag

import com.google.gson.annotations.SerializedName
import com.hook2book.hbsync.Model.Categories.CategoriesMain
import java.io.Serializable

public class CategoriesForListing  (@SerializedName("categoryDetail") var categoryDetail: CategoriesMain,
                                    @SerializedName("IsParent") var IsParent:String, @SerializedName("parentId") var parentId:Int?, @SerializedName("parentName") var parentName:String?):Serializable
{

}