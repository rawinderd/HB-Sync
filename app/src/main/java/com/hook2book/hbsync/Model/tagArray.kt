package com.sikhreader.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class tagArray(@SerializedName("tag_id") var tagId: Int? = null, @SerializedName("tag_name") var tagName: String? = null)
{
/*var tagName: String? = null) {
    @SerializedName("tag_id")
    @Expose
    var tagId: Int? = null

    @SerializedName("tag_name")
    @Expose
    var tagName: String? = null*/
}