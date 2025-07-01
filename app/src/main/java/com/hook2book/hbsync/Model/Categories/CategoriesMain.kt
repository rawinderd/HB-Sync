package com.sikhreader.Model.Categories

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.sikhreader.Model.CommonModels.Links
import java.io.Serializable


class CategoriesMain :Serializable{
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("slug")
    @Expose
    var slug: String? = null

    @SerializedName("parent")
    @Expose
    var parent: Int? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    /*@SerializedName("wc_category_id")
    @Expose
    var wc_category_id: String? = null*/

    @SerializedName("display")
    @Expose
    var display: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("menu_order")
    @Expose
    var menuOrder: Int? = null

    @SerializedName("count")
    @Expose
    var count: Int? = null

    @SerializedName("_links")
    @Expose
    var links: Links? = null
}