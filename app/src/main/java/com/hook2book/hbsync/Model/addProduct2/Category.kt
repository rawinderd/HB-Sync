package com.hook2book.hbsync.Model.NewTag

data class Category(
    val category_id: Int,
    val category_name: String,
    val created_at: String,
    val deleted_at: Any?,
    val product_id: Int,
    val updated_at: String,
    val wc_category_id: Any?
)