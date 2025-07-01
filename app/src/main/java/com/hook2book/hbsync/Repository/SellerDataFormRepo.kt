package com.sikhreader.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sikhreader.EnumClasses.ApiResult
import com.sikhreader.Interface.RetroService
import com.sikhreader.Model.SellerData.DataOuter
import com.sikhreader.Model.SellerData.SellerData
import com.sikhreader.Model.SellerData.SellerDataResponseStatus

class SellerDataFormRepo {
    var apiSellerDataResponseStatus: MutableLiveData<ApiResult<SellerDataResponseStatus>> =
        MutableLiveData()
    var apiSellerDataResponse: MutableLiveData<ApiResult<SellerData>> = MutableLiveData()
    suspend fun insertSellerData(sellerData: DataOuter) {
        val params = HashMap<String, String>()
        params.put("wc_id", sellerData.wc_id)
        params.put("name", sellerData.name)
        params.put("mob_number", sellerData.mob_number)
        params.put("firm_name", sellerData.firm_name)
        params.put("address", sellerData.address)
        params.put("sku_initial", sellerData.sku_initial)
        params.put("password", sellerData.password)
        params.put("user_type", sellerData.user_type.toString())
        val response = RetroService.retroInstance.insertSellerData(params)
        if (response.isSuccessful) {
            if (response.isSuccessful) {
                Log.e("Success Rawinder01", Gson().toJson(response.body()))
            } else Log.e("UnSuccess Rawinder01", Gson().toJson(response.errorBody()))
            apiSellerDataResponseStatus.postValue(
                ApiResult.Success(
                    response.body(),
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
            Log.i("TAG", "fetchParentCategories: " + response.body())

        } else {
            Log.e("UnSuccess Rawinder01", Gson().toJson(response.errorBody()))
            val errorMsg = response.errorBody()?.string()
            response.errorBody()
                ?.close()  // remember to close it after getting the stream of error body
            apiSellerDataResponseStatus.postValue(
                ApiResult.Error(
                    errorMsg,
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
        }
    }

    suspend fun fetchUserData(pubId: String) {
        val params = HashMap<String, String>()
        params.put("wc_id", pubId)
        val response = RetroService.retroInstance.getUserData(params)
        if (response.isSuccessful) {
            if (response.isSuccessful) {
                Log.e("Success Rawinder01 pub data", Gson().toJson(response.body()))
            } else Log.e("UnSuccess Rawinder01 pub data", Gson().toJson(response.errorBody()))
            apiSellerDataResponse.postValue(
                ApiResult.Success(
                    response.body(),
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
            Log.i("TAG", "fetchParentCategories: " + response.body())

        } else {
            val errorMsg = response.errorBody()?.string()
            response.errorBody()
                ?.close()  // remember to close it after getting the stream of error body
            apiSellerDataResponse.postValue(
                ApiResult.Error(
                    errorMsg,
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
        }
    }
}