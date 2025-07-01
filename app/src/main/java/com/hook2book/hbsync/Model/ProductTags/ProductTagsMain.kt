package com.example

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.sikhreader.Model.CommonModels.Links
import java.io.Serializable

class ProductTagsMain:Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    constructor(
        id: Int?,
        name: String?,
       // wc_tag_id:String?,
        slug: String?,
        description: String?,
        count: Int?,
        links: Links?
    ) {
        this.id = id
        this.name = name
        //
        this.slug = slug
        this.description = description
        this.count = count
        this.links = links
    }

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("slug")
    @Expose
    var slug: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

  /*  @SerializedName("wc_tag_id")
    @Expose
    var wc_tag_id: String? = null
*/
    @SerializedName("count")
    @Expose
    var count: Int? = null

    @SerializedName("_links")
    @Expose
    var links: Links? = null
}