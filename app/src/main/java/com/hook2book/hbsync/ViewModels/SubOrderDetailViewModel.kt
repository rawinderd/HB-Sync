package com.hook2book.hbsync.Model.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonArray
import com.hook2book.hbsync.EnumClasses.ApiResult
import com.hook2book.hbsync.Model.SingleSubOrderDetail.SingleSubOrderDetail
import com.hook2book.hbsync.Repository.SubOrderDetailRepo
import kotlinx.coroutines.launch

class SubOrderDetailViewModel : ViewModel() {
    private var subOrderDetailRepo: SubOrderDetailRepo = SubOrderDetailRepo()
    fun fetchSingleSubOrder(orderId: String) {
        viewModelScope.launch {
            subOrderDetailRepo.fetchSingleSubOrder(orderId)
        }
    }

    fun getSubOrderDetail(): MutableLiveData<ApiResult<SingleSubOrderDetail>> {
        return subOrderDetailRepo.apiResultSingleSubOrderDetail
    }

    fun submitOrderStatus(
        subOrderId: String, trackCode: String, courierCompany: String, date: String, status: String
    ) {

        viewModelScope.launch {
            subOrderDetailRepo.submitOrderStatus(
                subOrderId, trackCode, courierCompany, date, status
            )
        }
    }

    fun submitProductStatus(jsonArray: JsonArray) {
        viewModelScope.launch {

            subOrderDetailRepo.submitProductStatus(jsonArray)
        }
    }
}