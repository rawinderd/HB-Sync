package com.hook2book.hbsync.Model.Registration

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.hook2book.hbsync.Model.CommonModels.Links


class RegistrationResponse {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("date_created")
    @Expose
    var dateCreated: String? = null

    @SerializedName("date_created_gmt")
    @Expose
    var dateCreatedGmt: String? = null

    @SerializedName("date_modified")
    @Expose
    var dateModified: String? = null

    @SerializedName("date_modified_gmt")
    @Expose
    var dateModifiedGmt: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("first_name")
    @Expose
    var firstName: String? = null

    @SerializedName("last_name")
    @Expose
    var lastName: String? = null

    @SerializedName("role")
    @Expose
    var role: String? = null

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("billing")
    @Expose
    var billing: Billing? = null

    @SerializedName("shipping")
    @Expose
    var shipping: Shipping? = null

    @SerializedName("is_paying_customer")
    @Expose
    var isPayingCustomer: Boolean? = null

    @SerializedName("avatar_url")
    @Expose
    var avatarUrl: String? = null

    @SerializedName("meta_data")
    @Expose
    var metaData: List<Any>? = null

    @SerializedName("_links")
    @Expose
    var links: Links? = null
}