package com.hook2book.hbsync.Model.CommonModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Collection: Serializable {
    @SerializedName("href")
    @Expose
    var href: String? = null
}