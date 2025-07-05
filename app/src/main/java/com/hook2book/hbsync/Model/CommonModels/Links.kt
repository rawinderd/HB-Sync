package com.hook2book.hbsync.Model.CommonModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Links :Serializable{
    @SerializedName("self")
    @Expose
    var self: List<Self>? = null

    @SerializedName("collection")
    @Expose
    var collection: List<Collection>? = null
}