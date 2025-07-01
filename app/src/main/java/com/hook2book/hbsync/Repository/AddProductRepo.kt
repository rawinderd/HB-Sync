package com.sikhreader.Repository

import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sikhreader.EnumClasses.ApiResult
import com.sikhreader.Interface.RetroService
import com.sikhreader.Model.AddProduct.ApiResponse
import com.sikhreader.Model.SingleProduct.ProductMainSingle
import com.sikhreader.Model.UpdatePreStatus.UpdatePreStatus
import com.sikhreader.Model.addProduct2.ProductMain
import com.sikhreader.Model.updateWCProduct.updateWCProduct
import java.nio.charset.StandardCharsets


class AddProductRepo {
    var apiResult: MutableLiveData<ApiResult<ApiResponse>> = MutableLiveData()
    var UpdateApiResult: MutableLiveData<ApiResult<ApiResponse>> = MutableLiveData()
    var apiResultSingleProduct: MutableLiveData<ApiResult<ProductMainSingle>> = MutableLiveData()
    var apiResultUpdateWCProduct: MutableLiveData<ApiResult<updateWCProduct>> = MutableLiveData()
    var apiResultUpdatePreStatus: MutableLiveData<ApiResult<UpdatePreStatus>> = MutableLiveData()

    suspend fun addProductToDB1(makeObject: JsonObject) {
        Log.e("Success Rawinder01", Gson().toJson(makeObject))
        val ck = "ck_7fdbe299e87f8c839402fa4720538e2c948435aa"
        val cs = "cs_74da2c5beb91a60993eb8977d86e307e075a854f"
        val base = ck + ":" + cs
        val authHeader = "Basic " + Base64.encodeToString(
            base.toByteArray(StandardCharsets.UTF_8), Base64.NO_WRAP
        )
        Log.i("TAG", "addProductToDB1: "+makeObject.get("pub_seller_id"))
        Log.e("Success Rawinder201 updatejson1", Gson().toJson(makeObject))
        val response = RetroService.retroInstance.addProductToDB1(
            authHeader,
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
        val ck = "ck_7fdbe299e87f8c839402fa4720538e2c948435aa"
        val cs = "cs_74da2c5beb91a60993eb8977d86e307e075a854f"
        val base = ck + ":" + cs
        val authHeader = "Basic " + Base64.encodeToString(
            base.toByteArray(StandardCharsets.UTF_8), Base64.NO_WRAP
        )
        val response = RetroService.retroInstance.fetchSingleProduct(
            authHeader, id
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
        val ck = "ck_7fdbe299e87f8c839402fa4720538e2c948435aa"
        val cs = "cs_74da2c5beb91a60993eb8977d86e307e075a854f"
        val base = ck + ":" + cs
        val authHeader = "Basic " + Base64.encodeToString(
            base.toByteArray(StandardCharsets.UTF_8), Base64.NO_WRAP
        )
        Log.e("Success Rawinder201 updatejson", Gson().toJson(makeObject))
        val response = RetroService.retroInstance.updateProductToDB1(
            authHeader,
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
        val ck = "ck_7fdbe299e87f8c839402fa4720538e2c948435aa"
        val cs = "cs_74da2c5beb91a60993eb8977d86e307e075a854f"
        val base = ck + ":" + cs
        val authHeader = "Basic " + Base64.encodeToString(
            base.toByteArray(StandardCharsets.UTF_8), Base64.NO_WRAP
        )
        val params = HashMap<String, String>()
        params.put("status", status)
        val response = RetroService.retroInstance.updateProductStatusInWC(authHeader,wcProductId,params)
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

