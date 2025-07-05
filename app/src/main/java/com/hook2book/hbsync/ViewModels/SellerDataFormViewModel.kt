package com.hook2book.hbsync.Model.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hook2book.hbsync.EnumClasses.ApiResult
import com.hook2book.hbsync.Model.SellerData.DataOuter
import com.hook2book.hbsync.Model.SellerData.SellerDataResponseStatus
import com.hook2book.hbsync.Repository.SellerDataFormRepo
import kotlinx.coroutines.launch

class SellerDataFormViewModel : ViewModel() {
    private var sellerDataFormRepo: SellerDataFormRepo = SellerDataFormRepo()
    fun insertSellerData(sellerData: DataOuter) {
        viewModelScope.launch {
            sellerDataFormRepo.insertSellerData(sellerData)
        }
    }

    fun sellerDataStatus(): MutableLiveData<ApiResult<SellerDataResponseStatus>> {
        return sellerDataFormRepo.apiSellerDataResponseStatus
    }

}