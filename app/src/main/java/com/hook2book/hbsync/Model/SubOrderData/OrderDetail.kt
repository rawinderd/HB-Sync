package com.hook2book.hbsync.Model.SubOrderData

data class OrderDetail(
    val comment: String,
    val created_at: String,
    val image_src: Any?,
    val name: String,
    val order_detail_id: Int,
    val order_id: String,
    val price: Int,
    val product_id: String,
    val quantity: Int,
    val sku: String,
    val status: Int,
    val sub_order_product_id: String,
    val total: String,
    val updated_at: String
)