package com.sikhreader.Model.Categories

import com.google.gson.annotations.SerializedName
import java.io.Serializable

public class CategoriesForListing  (@SerializedName("categoryDetail") var categoryDetail:CategoriesMain,
                                    @SerializedName("IsParent") var IsParent:String, @SerializedName("parentId") var parentId:Int?, @SerializedName("parentName") var parentName:String?):Serializable
{

}