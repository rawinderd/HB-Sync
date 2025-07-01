package com.sikhreader.Model.SubOrderData

data class Data(
    val address_1: String,
    val address_2: String,
    val city: String,
    val comment: String,
    val country: String,
    val created_at: String,
    val deleted_at: String,
    val first_name: String,
    val last_name: String,
    val order_detail: List<OrderDetail>,
    val order_id: Int,
    val order_status: Int,
    val phone: String,
    val postcode: String,
    val pub_id: String,
    val shipping_company: String,
    val shipping_date: String,
    val state: String,
    val sub_order_id: String,
    val track_code: String,
    val updated_at: String
)