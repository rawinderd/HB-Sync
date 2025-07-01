package com.sikhreader.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sikhreader.EnumClasses.ApiResult
import com.sikhreader.Model.SellerData.DataOuter
import com.sikhreader.Model.SellerData.SellerDataResponseStatus
import com.sikhreader.Repository.SellerDataFormRepo
import kotlinx.coroutines.launch

class SellerDataFormViewModel : ViewModel() {
    private var sellerDataFormRepo: SellerDataFormRepo = SellerDataFormRepo()
    fun insertSellerData(sellerData: DataOuter) {
        viewModelScope.launch {
            sellerDataFormRepo.insertSellerData(sellerData)
        }
    }
    fun sellerDataStatus(): MutableLiveData<ApiResult<SellerDataResponseStatus>>
    {
        return sellerDataFormRepo.apiSellerDataResponseStatus
    }

}