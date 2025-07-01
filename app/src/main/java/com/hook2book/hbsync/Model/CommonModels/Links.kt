package com.sikhreader.Model.CommonModels

import com.example.Self
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