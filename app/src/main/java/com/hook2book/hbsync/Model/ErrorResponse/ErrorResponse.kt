package com.hook2book.hbsync.Model.NewTag

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class ErrorResponse {
    @SerializedName("code")
    @Expose
    var code: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null
}