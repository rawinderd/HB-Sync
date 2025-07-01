package com.sikhreader.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ProductTagsMain
import com.sikhreader.EnumClasses.ApiResult
import com.sikhreader.Model.Categories.CategoriesMain
import com.sikhreader.Repository.ProductCategoriesRepo
import kotlinx.coroutines.launch

class ProductCategoriesViewModel:ViewModel() {
    var productCategoriesRepo:ProductCategoriesRepo= ProductCategoriesRepo()

    fun fetchCategories() {
        viewModelScope.launch {
            productCategoriesRepo.fetchCategories()
        }
    }
    fun getCategoriesList(): LiveData<ApiResult<List<CategoriesMain>>> {
        return productCategoriesRepo.apiResult

    }
}