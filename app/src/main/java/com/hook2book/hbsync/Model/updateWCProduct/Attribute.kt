package com.sikhreader.Model.updateWCProduct

data class Attribute(
    val id: Int,
    val name: String,
    val options: List<String>,
    val position: Int,
    val slug: String,
    val variation: Boolean,
    val visible: Boolean
)