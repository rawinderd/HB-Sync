package com.hook2book.hbsync.Model.Registration
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Shipping {
    constructor(
        firstName: String?,
        lastName: String?,
        company: String?,
        address1: String?,
        address2: String?,
        city: String?,
        postcode: String?,
        country: String?,
        state: String?,
        email: String?,
        phone: String?
    ) {
        this.firstName = firstName
        this.lastName = lastName
        this.company = company
        this.address1 = address1
        this.address2 = address2
        this.city = city
        this.postcode = postcode
        this.country = country
        this.state = state
        this.email = email
        this.phone = phone
    }

    constructor()

    @SerializedName("first_name")
    @Expose
    var firstName: String? = null

    @SerializedName("last_name")
    @Expose
    var lastName: String? = null

    @SerializedName("company")
    @Expose
    var company: String? = null

    @SerializedName("address_1")
    @Expose
    var address1: String? = null

    @SerializedName("address_2")
    @Expose
    var address2: String? = null

    @SerializedName("city")
    @Expose
    var city: String? = null

    @SerializedName("postcode")
    @Expose
    var postcode: String? = null

    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("state")
    @Expose
    var state: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null
}
