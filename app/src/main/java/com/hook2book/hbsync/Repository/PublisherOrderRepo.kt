package com.hook2book.hbsync.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hook2book.hbsync.EnumClasses.ApiResult
import com.hook2book.hbsync.Interface.RetroService
import com.hook2book.hbsync.Model.SubOrderData.SubOrderData

class PublisherOrderRepo {
    var apiSubOrders: MutableLiveData<ApiResult<SubOrderData>> = MutableLiveData()
    suspend fun fetchSubOrders(skuInitial: String) {
        val params = HashMap<String, String>()
        params.put("pub_id", skuInitial)
        Log.i("TAG", "fetchSubOrders:sku " + skuInitial)
        //params.put("per_page", "15")
        // params.put("order", "desc")*/

        val response = RetroService.retroInstance.fetchSubOrders(params)
        if (response.isSuccessful) {
            apiSubOrders.postValue(
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
            apiSubOrders.postValue(
                ApiResult.Error(
                    errorMsg,
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
        }
    }
}