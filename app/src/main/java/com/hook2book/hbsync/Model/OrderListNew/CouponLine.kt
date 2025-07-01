package com.sikhreader.Model.OrderListNew

import java.io.Serializable

data class CouponLine(
    val code: String,
    val discount: String,
    val discount_tax: String,
    val discount_type: String,
    val free_shipping: Boolean,
    val id: Int,
    val meta_data: List<MetaData>,
    val nominal_amount: Int
):Serializable