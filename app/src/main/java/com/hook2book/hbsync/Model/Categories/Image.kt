package com.hook2book.hbsync.Model.Categories

import java.io.Serializable

data class Image(
    val alt: String,
    val date_created: String,
    val date_created_gmt: String,
    val date_modified: String,
    val date_modified_gmt: String,
    val id: Int,
    val name: String,
    val src: String
):Serializable