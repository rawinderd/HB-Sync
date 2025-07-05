package com.hook2book.hbsync.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hook2book.hbsync.EnumClasses.ApiResult
import com.hook2book.hbsync.Model.SubOrderData.SubOrderData
import com.hook2book.hbsync.Repository.PublisherOrderRepo
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