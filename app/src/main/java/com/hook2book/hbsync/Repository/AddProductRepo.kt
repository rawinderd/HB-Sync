package com.hook2book.hbsync.Repository

import android.app.Application
import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.hook2book.hbsync.EnumClasses.ApiResult
import com.hook2book.hbsync.Interface.RetroService
import com.hook2book.hbsync.Model.NewTag.ApiResponse
import com.hook2book.hbsync.Model.SingleProduct.ProductMainSingle
import com.hook2book.hbsync.Model.UpdatePreStatus.UpdatePreStatus
import com.hook2book.hbsync.Model.updateWCProduct.updateWCProduct
import com.hook2book.hbsync.UtilityClass.Preferences

import java.nio.charset.StandardCharsets


class AddProductRepo(application: Application) {
    var apiResult: MutableLiveData<ApiResult<ApiResponse>> = MutableLiveData()
    var UpdateApiResult: MutableLiveData<ApiResult<ApiResponse>> = MutableLiveData()
    var apiResultSingleProduct: MutableLiveData<ApiResult<ProductMainSingle>> = MutableLiveData()
    var apiResultUpdateWCProduct: MutableLiveData<ApiResult<updateWCProduct>> = MutableLiveData()
    var apiResultUpdatePreStatus: MutableLiveData<ApiResult<UpdatePreStatus>> = MutableLiveData()
    var application: Application = application

    suspend fun addProductToDB1(makeObject: JsonObject) {
        val response = RetroService.retroInstance.addProductToDB1(
            Preferences.loadCombinedKey(application),
            makeObject
        )
        if (response.isSuccessful) {

            apiResult.postValue(
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
                ?.close()   // remember to close it after getting the stream of error body
            apiResult.postValue(
                ApiResult.Error(
                    errorMsg,
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
            if (response.isSuccessful) {
                Log.e("Success Rawinder101", Gson().toJson(response.body()))
            } else Log.e("UnSuccess Rawinder101", Gson().toJson(response.errorBody()))
        }
    }

    suspend fun fetchSingleProductData(id: String) {
        val response = RetroService.retroInstance.fetchSingleProduct(
            Preferences.loadCombinedKey(application), id
        )
        if (response.isSuccessful) {
            Log.e("Success Rawinder52", Gson().toJson(response.body()))

            apiResultSingleProduct.postValue(
                ApiResult.Success(
                    response.body(),
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )

        } else {
            Log.e("UnSuccess Rawinder52", Gson().toJson(response.errorBody()))
            val errorMsg = response.errorBody()?.string()
            response.errorBody()
                ?.close()   // remember to close it after getting the stream of error body
            apiResultSingleProduct.postValue(
                ApiResult.Error(
                    errorMsg,
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
        }
    }

    suspend fun updateProduct(makeObject: JsonObject, productId: String) {


        val response = RetroService.retroInstance.updateProductToDB1(
            Preferences.loadCombinedKey(application),
            makeObject,productId
        )
        if (response.isSuccessful) {

            UpdateApiResult.postValue(
                ApiResult.Success(
                    response.body(),
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
            if (response.isSuccessful) {
                Log.e("Success Rawinder201", Gson().toJson(response.body()))
            } else Log.e("UnSuccess Rawinder201", Gson().toJson(response.errorBody()))
        } else {
            val errorMsg = response.errorBody()?.string()
            response.errorBody()
                ?.close()   // remember to close it after getting the stream of error body
            UpdateApiResult.postValue(
                ApiResult.Error(
                    errorMsg,
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
            if (response.isSuccessful) {
                Log.e("Success Rawinder301", Gson().toJson(response.body()))
            } else Log.e("UnSuccess Rawinder401", Gson().toJson(response.errorBody()))
        }
    }

    suspend fun updateProductStatusInWC(status: String, wcProductId: String) {

        val params = HashMap<String, String>()
        params.put("status", status)
        val response = RetroService.retroInstance.updateProductStatusInWC(Preferences.loadCombinedKey(application),wcProductId,params)
        if (response.isSuccessful) {
            apiResultUpdateWCProduct.postValue(
                ApiResult.Success(
                    response.body(),
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
            if (response.isSuccessful) {
                Log.e("Success Rawinder201", Gson().toJson(response.body()))
            } else Log.e("UnSuccess Rawinder201", Gson().toJson(response.errorBody()))
        } else {
            val errorMsg = response.errorBody()?.string()
            response.errorBody()
                ?.close()   // remember to close it after getting the stream of error body
            apiResultUpdateWCProduct.postValue(
                ApiResult.Error(
                    errorMsg,
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
            if (response.isSuccessful) {
                Log.e("Success Rawinder301", Gson().toJson(response.body()))
            } else Log.e("UnSuccess Rawinder401", Gson().toJson(response.errorBody()))
        }

    }

    suspend fun updateStatusInPre(status: String, productId: String) {

        val params = HashMap<String, String>()
        params.put("status", status)
        val response = RetroService.retroInstance.updateStatusInPre(productId, params)
        if (response.isSuccessful) {
            apiResultUpdatePreStatus.postValue(
                ApiResult.Success(
                    response.body(),
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
            if (response.isSuccessful) {
                Log.e("Success Rawinder201", Gson().toJson(response.body()))
            } else Log.e("UnSuccess Rawinder201", Gson().toJson(response.errorBody()))
        } else {
            val errorMsg = response.errorBody()?.string()
            response.errorBody()
                ?.close()   // remember to close it after getting the stream of error body
            apiResultUpdatePreStatus.postValue(
                ApiResult.Error(
                    errorMsg,
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
            if (response.isSuccessful) {
                Log.e("Success Rawinder301", Gson().toJson(response.body()))
            } else Log.e("UnSuccess Rawinder401", Gson().toJson(response.errorBody()))
        }
    }
}

