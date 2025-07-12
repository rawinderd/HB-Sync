package com.hook2book.hbsync.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hook2book.hbsync.EnumClasses.ApiResult
import com.hook2book.hbsync.Model.Categories.CategoriesMain
import com.hook2book.hbsync.Repository.ProductCategoriesRepo
import kotlinx.coroutines.launch

class ProductCategoriesViewModel(application: Application) : AndroidViewModel(application) {
    var productCategoriesRepo: ProductCategoriesRepo = ProductCategoriesRepo(application)


    fun fetchCategories() {
        viewModelScope.launch {
            productCategoriesRepo.fetchCategories()
        }
    }

    fun getCategoriesList(): LiveData<ApiResult<List<CategoriesMain>>> {
        return productCategoriesRepo.apiResult
    }
}