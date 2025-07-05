package com.hook2book.hbsync.Model.NewTag

import java.io.Serializable

data class ShippingLine(
    val id: Int,
    val instance_id: String,
    val meta_data: List<MetaData>,
    val method_id: String,
    val method_title: String,
    val taxes: List<Any?>,
    val total: String,
    val total_tax: String
):Serializable