package com.sikhreader.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sikhreader.EnumClasses.ApiResult
import com.sikhreader.Model.SellerData.SellerData
import com.sikhreader.Repository.SellerDataFormRepo
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var sellerDataFormRepo: SellerDataFormRepo = SellerDataFormRepo()
    fun fetchUserData(pubId: String) {
        viewModelScope.launch {
            sellerDataFormRepo.fetchUserData(pubId)
        }
    }

    fun getUserData(): MutableLiveData<ApiResult<SellerData>>
    {
        return sellerDataFormRepo.apiSellerDataResponse
    }
}