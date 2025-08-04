package com.hook2book.hbsync.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hook2book.hbsync.EnumClasses.ApiResult
import com.hook2book.hbsync.Model.Categories.CategoriesMain
import com.hook2book.hbsync.Model.SellerData.SellerData
import com.hook2book.hbsync.Repository.ProductCategoriesRepo
import com.hook2book.hbsync.Repository.SellerDataFormRepo
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var sellerDataFormRepo: SellerDataFormRepo = SellerDataFormRepo()
    var productCategoriesRepo: ProductCategoriesRepo = ProductCategoriesRepo(application)
    fun fetchUserData(pubId: String) {
        viewModelScope.launch {
            sellerDataFormRepo.fetchUserData(pubId)
        }
    }

    fun getUserData(): MutableLiveData<ApiResult<SellerData>> {
        return sellerDataFormRepo.apiSellerDataResponse
    }



    fun fetchCategories() {
        viewModelScope.launch {
            productCategoriesRepo.fetchCategories()
        }
    }

    fun getCategoriesList(): LiveData<ApiResult<List<CategoriesMain>>> {
        return productCategoriesRepo.apiResult
    }
}