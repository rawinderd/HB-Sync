package com.hook2book.hbsync.Model.NewTag

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ApiResponse {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}