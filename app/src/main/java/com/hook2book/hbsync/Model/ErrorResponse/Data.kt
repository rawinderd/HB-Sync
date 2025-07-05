package com.hook2book.hbsync.Model.ErrorResponse


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Data {
    @SerializedName("status")
    @Expose
    var status: Int? = null
}