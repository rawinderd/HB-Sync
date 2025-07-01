package com.sikhreader.Model.Temp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ProductMainTemp {
    @SerializedName("product_id")
    @Expose
    var productId: Int? = null

    @SerializedName("book_name")
    @Expose
    var bookName: String? = null

    @SerializedName("writer_name")
    @Expose
    var writerName: String? = null

    @SerializedName("pub_seller_id")
    @Expose
    var pubSellerId: String? = null

    @SerializedName("pages")
    @Expose
    var pages: String? = null

    @SerializedName("format")
    @Expose
    var format: String? = null

    @SerializedName("language")
    @Expose
    var language: String? = null

    @SerializedName("wc_product_id")
    @Expose
    var wcProductId: String? = null

    @SerializedName("price")
    @Expose
    var price: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("stock")
    @Expose
    var stock: String? = null

    @SerializedName("short_description")
    @Expose
    var shortDescription: String? = null

    @SerializedName("product_type")
    @Expose
    var productType: String? = null

    @SerializedName("weight")
    @Expose
    var weight: String? = null

    @SerializedName("height")
    @Expose
    var height: String? = null

    @SerializedName("width")
    @Expose
    var width: String? = null

    @SerializedName("length")
    @Expose
    var length: String? = null

    @SerializedName("shipping_class")
    @Expose
    var shippingClass: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("visibility")
    @Expose
    var visibility: String? = null

    @SerializedName("remarks")
    @Expose
    var remarks: String? = null

    @SerializedName("related")
    @Expose
    var related: String? = null

    @SerializedName("tags")
    @Expose
    var tags: Any? = null

    @SerializedName("user_type")
    @Expose
    var userType: Int? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("deleted_at")
    @Expose
    var deletedAt: Any? = null
}