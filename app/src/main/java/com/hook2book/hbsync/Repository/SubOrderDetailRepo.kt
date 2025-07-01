package com.sikhreader.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.sikhreader.EnumClasses.ApiResult
import com.sikhreader.Interface.RetroService
import com.sikhreader.Model.SingleSubOrderDetail.SingleSubOrderDetail
import com.sikhreader.Model.productStatusUpdate.productStatusUpdate
import com.sikhreader.Model.subOrderUpdate.subOrderUpdate

class SubOrderDetailRepo {
    var apiResultSingleSubOrderDetail: MutableLiveData<ApiResult<SingleSubOrderDetail>> =
        MutableLiveData()
    var apiResultSubOrderStatusResponse: MutableLiveData<ApiResult<subOrderUpdate>> =
        MutableLiveData()
    var apiResultproductStatusUpdateResponse: MutableLiveData<ApiResult<productStatusUpdate>> =
        MutableLiveData()

    suspend fun fetchSingleSubOrder(orderId: String) {
        val response = RetroService.retroInstance.fetchSingleSubOrder(orderId)
        if (response.isSuccessful) {
            apiResultSingleSubOrderDetail.postValue(
                ApiResult.Success(
                    response.body(),
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
            if (response.isSuccessful) {
                Log.e("Success Rawinder01", Gson().toJson(response.body()))
            } else Log.e("UnSuccess Rawinder01", Gson().toJson(response.errorBody()))
        } else {
            val errorMsg = response.errorBody()?.string()
            response.errorBody()
                ?.close()  // remember to close it after getting the stream of error body
            apiResultSingleSubOrderDetail.postValue(
                ApiResult.Error(
                    errorMsg,
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
        }
    }

    suspend fun submitOrderStatus(
        subOrderId: String,
        trackCode: String,
        courierCompany: String,
        date: String,
        status: String
    ) {
        val params = HashMap<String, String>()
        params.put("status", status)
        params.put("track_code", trackCode)
        params.put("shipping_company", courierCompany)
        params.put("shipping_date", date)
        Log.i(
            "TAG",
            "submitOrderStatus: subOrderId: $subOrderId, trackCode: $trackCode, courierCompany: $courierCompany, date: $date, status: $status"
        )/*  val jsonObject = JsonObject()
        jsonObject.addProperty("status", status)
        jsonObject.addProperty("track_code", trackCode)
        jsonObject.addProperty("shipping_company", courierCompany)
        jsonObject.addProperty("shipping_date", date)*/

        //val response = RetroService.retroInstance.submitOrderStatus(subOrderId, jsonObject)
        val response = RetroService.retroInstance.submitOrderStatus(subOrderId, params)
        if (response.isSuccessful) {
            apiResultSubOrderStatusResponse.postValue(
                ApiResult.Success(
                    response.body(),
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
            if (response.isSuccessful) {
                Log.e("Success Rawinder011", Gson().toJson(response.body()))
            } else Log.e("UnSuccess Rawinder011", Gson().toJson(response.errorBody()))
        } else {
            val errorMsg = response.errorBody()?.string()
            response.errorBody()
                ?.close()  // remember to close it after getting the stream of error body
            apiResultSubOrderStatusResponse.postValue(
                ApiResult.Error(
                    errorMsg,
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
        }
    }

    suspend fun submitProductStatus(jsonArray: JsonArray) {
       // var jsonObject = JsonObject()
       // jsonObject.add("products", jsonArray)
        val response = RetroService.retroInstance.submitProductStatus(jsonArray)
        if (response.isSuccessful) {
            apiResultproductStatusUpdateResponse.postValue(
                ApiResult.Success(
                    response.body(),
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
            if (response.isSuccessful) {
                Log.e("Success Rawinder2201", Gson().toJson(response.body()))
            } else Log.e("UnSuccess Rawinder2201", Gson().toJson(response.errorBody()))
        } else {
            val errorMsg = response.errorBody()?.string()
            response.errorBody()
                ?.close()  // remember to close it after getting the stream of error body
            apiResultproductStatusUpdateResponse.postValue(
                ApiResult.Error(
                    errorMsg,
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
        }
    }
}