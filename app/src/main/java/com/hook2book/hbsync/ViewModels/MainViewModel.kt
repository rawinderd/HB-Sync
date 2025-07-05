package com.hook2book.hbsync.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hook2book.hbsync.EnumClasses.ApiResult
import com.hook2book.hbsync.Model.SellerData.SellerData
import com.hook2book.hbsync.Repository.SellerDataFormRepo
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var sellerDataFormRepo: SellerDataFormRepo = SellerDataFormRepo()
    fun fetchUserData(pubId: String) {
        viewModelScope.launch {
            sellerDataFormRepo.fetchUserData(pubId)
        }
    }

    fun getUserData(): MutableLiveData<ApiResult<SellerData>> {
        return sellerDataFormRepo.apiSellerDataResponse
    }
}