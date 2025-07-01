package com.sikhreader.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sikhreader.EnumClasses.ApiResult
import com.sikhreader.Model.SubOrderData.SubOrderData
import com.sikhreader.Repository.PublisherOrderRepo
import kotlinx.coroutines.launch

class PublisherOrderViewModel : ViewModel() {
    private var publisherOrderRepo: PublisherOrderRepo = PublisherOrderRepo()
    fun fetchSubOrders(skuInitial: String) {
        viewModelScope.launch {
            publisherOrderRepo.fetchSubOrders(skuInitial)
        }
    }

    fun getSubOrders(): MutableLiveData<ApiResult<SubOrderData>> {
        return publisherOrderRepo.apiSubOrders
    }
}