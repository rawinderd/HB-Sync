package com.sikhreader.Model.OrderListNew

import java.io.Serializable

data class LineItem(
    val id: Int,
    val image: Image,
    val meta_data: List<MetaData>,
    val name: String,
    val parent_name: Any?,
    val price: Int,
    val product_id: Int,
    val quantity: Int,
    val sku: String,
    val subtotal: String,
    val subtotal_tax: String,
    val tax_class: String,
    val taxes: List<Any?>,
    val total: String,
    val total_tax: String,
    val variation_id: Int
):Serializable