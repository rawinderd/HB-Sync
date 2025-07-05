package com.hook2book.hbsync.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hook2book.hbsync.EnumClasses.ApiResult
import com.hook2book.hbsync.Model.SearchProduct.SearchProduct
import com.hook2book.hbsync.Repository.ProductListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    var productRepo = ProductListRepo(application)/* val productsLiveData: LiveData<SearchProduct>
         get() = productRepo.products*/

    fun fetchProducts(searchString: String, pubId: String, productStatus: String, pageNo: Int) {
        viewModelScope.launch {
            productRepo.fetchProducts(searchString, pubId, productStatus, pageNo)
        }
    }

    fun getProductList(): MutableLiveData<ApiResult<SearchProduct>> {
        return productRepo.apiResultSearchProduct
    }
}
