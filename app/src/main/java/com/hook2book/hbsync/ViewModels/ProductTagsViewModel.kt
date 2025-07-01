package com.sikhreader.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ProductTagsMain
import com.sikhreader.EnumClasses.ApiResult
import com.sikhreader.Model.NewTag.newTag
import com.sikhreader.Repository.ProductsTagsRepo
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductTagsViewModel @Inject constructor() : ViewModel() {
    var productsTagsRepo = ProductsTagsRepo()

    fun fetchTagList(keyword:String, pageNumber: Int) {
        viewModelScope.launch {
            productsTagsRepo.getTagList(keyword, pageNumber)
        }
    }
    /*suspend fun FetchTagByKeyword(keyword:String, pageNumber: Int)
    {
        productsTagsRepo.getTagList(keyword, pageNumber)
    }*/
    fun getTagList(): LiveData<ApiResult<List<ProductTagsMain>>> {
        return productsTagsRepo.apiResult

    }

    fun createNewTag(newTagText: String) {
        viewModelScope.launch {
            productsTagsRepo.createNewTag(newTagText)
        }
    }
    fun getNewTag(): LiveData<ApiResult<newTag>> {
        return productsTagsRepo.apiResultNewTag

    }

}


