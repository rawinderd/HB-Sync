package com.hook2book.hbsync.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.hook2book.hbsync.EnumClasses.ApiResult
import com.hook2book.hbsync.Model.NewTag.ApiResponse
import com.hook2book.hbsync.Model.NewTag.newTag
import com.hook2book.hbsync.Model.ProductTags.ProductTagsMain
import com.hook2book.hbsync.Model.SingleProduct.ProductMainSingle
import com.hook2book.hbsync.Model.UpdatePreStatus.UpdatePreStatus
import com.hook2book.hbsync.Model.updateWCProduct.updateWCProduct
import com.hook2book.hbsync.Repository.AddProductRepo
import com.hook2book.hbsync.Repository.ProductsTagsRepo

import kotlinx.coroutines.launch

class AddProductViewModel(application: Application) : AndroidViewModel(application) {

    var addProductRepo: AddProductRepo = AddProductRepo(application)
    var productsTagsRepo: ProductsTagsRepo = ProductsTagsRepo(application)

    fun addProductStatusInDB(): LiveData<ApiResult<ApiResponse>> {
        return addProductRepo.apiResult

    }

    fun addProductToDB1(makeObject: JsonObject) {
        viewModelScope.launch {
            addProductRepo.addProductToDB1(makeObject)
        }
    }

    fun addProductResPonse(): MutableLiveData<ApiResult<ApiResponse>> {
        return addProductRepo.apiResult

    }

    fun fetchSingleProductData(id: String) {
        viewModelScope.launch {
            addProductRepo.fetchSingleProductData(id)
        }
    }

    fun addSingleProductResPonse(): MutableLiveData<ApiResult<ProductMainSingle>> {
        return addProductRepo.apiResultSingleProduct

    }

    fun updateProduct(mapProductDataToJsonObject: JsonObject, productId: String) {
        viewModelScope.launch {
            addProductRepo.updateProduct(mapProductDataToJsonObject, productId)

        }
    }

    fun updateProductResPonse(): MutableLiveData<ApiResult<ApiResponse>> {
        return addProductRepo.UpdateApiResult

    }

    fun updateStatusInWC(status: String, wcProductId: String) {
        viewModelScope.launch {
            addProductRepo.updateProductStatusInWC(status, wcProductId)

        }
    }

    fun updateStatusInWCResponse(): MutableLiveData<ApiResult<updateWCProduct>> {
        return addProductRepo.apiResultUpdateWCProduct

    }

    fun updateStatusInPre(status: String, productId: String) {
        viewModelScope.launch {
            addProductRepo.updateStatusInPre(status, productId)

        }
    }

    fun updateStatusInPreResponse(): MutableLiveData<ApiResult<UpdatePreStatus>> {
        return addProductRepo.apiResultUpdatePreStatus

    }

    fun searchWriterTagToAdd(keyword: String, pageNo: Int) {
        viewModelScope.launch {
            productsTagsRepo.getTagList(keyword, pageNo)
        }
    }

    fun getSearchedWriterTag(): LiveData<ApiResult<List<ProductTagsMain>>> {
        return productsTagsRepo.apiResult

    }

    fun createWriterNameTag(keyword: String) {
        viewModelScope.launch {
            productsTagsRepo.createNewTag(keyword)
        }
    }

    fun getNewWriterNameTag(): LiveData<ApiResult<newTag>> {
        return productsTagsRepo.apiResultNewTag

    }

    fun searchTagToAddLangauage(keyword: String, pageNo: Int) {
        viewModelScope.launch {
            productsTagsRepo.getTagListLanguage(keyword, pageNo)
        }
    }

    fun getSearchedTagLanguage(): LiveData<ApiResult<List<ProductTagsMain>>> {
        return productsTagsRepo.apiResultLanguageTag

    }

    fun searchTagToAddCategory(keyword: String, pageNo: Int) {
        viewModelScope.launch {
            productsTagsRepo.getTagListCategory(keyword, pageNo)
        }
    }

    fun getSearchedTagCategory(): LiveData<ApiResult<List<ProductTagsMain>>> {
        return productsTagsRepo.apiResultCategoryTag

    }


}