package com.sikhreader.Model.Temp
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ProductTemp {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("product")
    @Expose
    var product: ProductMainTemp? = null
}