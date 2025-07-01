package com.sikhreader.Model.Registration

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegistrationData(
    var email: String,
    var first_name: String,
    var last_name: String,
    var password: String,
    var username: String,
    billing: Billing,
    shipping: Shipping
) {
    fun getBilling(): Billing {
        return billing
    }

    fun setBilling(billing: Billing) {
        this.billing = billing
    }

    fun getShipping(): Shipping {
        return shipping
    }

    fun setShipping(shipping: Shipping) {
        this.shipping = shipping
    }

    @SerializedName("billing")
    @Expose
    private var billing: Billing

    @SerializedName("shipping")
    @Expose
    private var shipping: Shipping

    init {
        this.billing = billing
        this.shipping = shipping
    }
}
