package com.hook2book.hbsync.Model.Categories

import java.io.Serializable


data class LocalCategories(var count: Int,
                      var description: String,
                      var id: Int,
                      var CategoryName: String,
                      var CategoryParent: Int): Serializable