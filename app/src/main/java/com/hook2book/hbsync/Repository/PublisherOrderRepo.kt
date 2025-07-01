package com.sikhreader.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sikhreader.EnumClasses.ApiResult
import com.sikhreader.Interface.RetroService
import com.sikhreader.Model.SubOrderData.SubOrderData

class PublisherOrderRepo {
    var apiSubOrders: MutableLiveData<ApiResult<SubOrderData>> = MutableLiveData()
    suspend fun fetchSubOrders(skuInitial: String) {/*val ck = "ck_7fdbe299e87f8c839402fa4720538e2c948435aa"
        val cs = "cs_74da2c5beb91a60993eb8977d86e307e075a854f"
        val base = ck + ":" + cs
        val authHeader = "Basic " + Base64.encodeToString(
            base.toByteArray(StandardCharsets.UTF_8),
            Base64.NO_WRAP
        )*/
        val params = HashMap<String, String>()/*if (keyword != "None") {
             params.put("search",keyword)
         }*/
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