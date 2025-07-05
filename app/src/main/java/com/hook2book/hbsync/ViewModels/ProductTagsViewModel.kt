package com.hook2book.hbsync.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hook2book.hbsync.EnumClasses.ApiResult
import com.hook2book.hbsync.Model.NewTag.newTag
import com.hook2book.hbsync.Model.ProductTags.ProductTagsMain
import com.hook2book.hbsync.Repository.ProductsTagsRepo
import kotlinx.coroutines.launch

class ProductTagsViewModel(application: Application) : AndroidViewModel(application) {
    var productsTagsRepo = ProductsTagsRepo(application)

    fun fetchTagList(keyword: String, pageNumber: Int) {
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


