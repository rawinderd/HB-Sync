package com.sikhreader.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sikhreader.EnumClasses.ApiResult
import com.sikhreader.Model.SearchProduct.SearchProduct
import com.sikhreader.Repository.ProductListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor() : ViewModel() {

    var productRepo = ProductListRepo()
    /* val productsLiveData: LiveData<SearchProduct>
         get() = productRepo.products*/

    fun fetchProducts(searchString: String, pubId: String, productStatus: String, pageNo: Int) {
        viewModelScope.launch {
            productRepo.fetchProducts(searchString, pubId, productStatus,pageNo)
        }
    }

    fun getProductList(): MutableLiveData<ApiResult<SearchProduct>> {
        return productRepo.apiResultSearchProduct
    }
}
