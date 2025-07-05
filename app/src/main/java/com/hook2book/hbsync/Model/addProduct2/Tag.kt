package com.hook2book.hbsync.Model.NewTag

data class Tag(
    val created_at: String,
    val deleted_at: Any?,
    val product_id: Int,
    val tag_id: Int,
    val tag_name: String,
    val updated_at: String,
    val wc_tag_id: Any?
)