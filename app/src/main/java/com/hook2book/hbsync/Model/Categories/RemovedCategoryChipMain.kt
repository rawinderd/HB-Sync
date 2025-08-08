package com.hook2book.hbsync.Model.Categories

import com.hook2book.hbsync.Model.NewTag.CategoriesForListing2
import java.io.Serializable


data class RemovedCategoryChipMain(var ChipPosition: Int, var categoriesForListing: CategoriesForListing2, var CategoryPosition: Int):
    Serializable