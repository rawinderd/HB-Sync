package com.example

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TargetHints : Serializable {
    @SerializedName("allow")
    @Expose
    var allow: List<String>? = null
}