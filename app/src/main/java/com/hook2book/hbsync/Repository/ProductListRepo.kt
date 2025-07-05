package com.hook2book.hbsync.Repository
import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hook2book.hbsync.EnumClasses.ApiResult
import com.hook2book.hbsync.Interface.RetroService
import com.hook2book.hbsync.Model.SearchProduct.SearchProduct



class ProductListRepo(application: Application) {

    var apiResultSearchProduct: MutableLiveData<ApiResult<SearchProduct>> = MutableLiveData()
    var application: Application = application

    suspend fun fetchProducts(
        searchString: String,
        pubId: String,
        productStatus: String,
        pageNo: Int
    ) {
        val params = HashMap<String, String>()
        if (!searchString.isEmpty()) {
            if(!searchString.equals("None")) {
                params.put("book_name", searchString)
                Log.i("TAG", "fetchProducts: " + searchString)
            }
        }
        if (!pubId.isEmpty()) {
            params.put("pub_seller_id", pubId)
            Log.i("TAG", "pub Id: "+pubId)

        }
        /*if (!productStatus.isEmpty()) {
            params.put("status", productStatus)
        }*/
        params.put("page",pageNo.toString())

        val response = RetroService.retroInstance.fetchProducts(params)
        if (response.isSuccessful) {
            if (response.isSuccessful) {
                Log.e("Success Rawinder01", Gson().toJson(response.body()))
            } else Log.e("UnSuccess Rawinder01", Gson().toJson(response.errorBody()))
            apiResultSearchProduct.postValue(
                ApiResult.Success(
                    response.body(),
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
        } else {
            if (response.isSuccessful) {
                Log.e("Success Rawinder01", Gson().toJson(response.body()))
            } else Log.e("UnSuccess Rawinder01", Gson().toJson(response.errorBody()))
            val errorMsg = response.errorBody()?.string()
            Log.i("TAG", "allowAccess: " + errorMsg)
            response.errorBody()
                ?.close()  // remember to close it after getting the stream of error body
            apiResultSearchProduct.postValue(
                ApiResult.Error(
                    errorMsg,
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
        }
    }
}