package com.hook2book.hbsync.Model.NewTag

import java.io.Serializable

data class Billing(
    val address_1: String,
    val address_2: String,
    val city: String,
    val company: String,
    val country: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val postcode: String,
    val state: String
):Serializable