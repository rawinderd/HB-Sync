package com.hook2book.hbsync.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hook2book.hbsync.Model.InitialVaribales.InitialVariablesMain
import com.sikhreader.EnumClasses.ApiResult
import com.sikhreader.Interface.RetroService

class SplashRepo {
    var apiResultInitialValues: MutableLiveData<ApiResult<InitialVariablesMain>> = MutableLiveData()
    suspend fun fetchInitialValues() {

        val response = RetroService.retroInstance.fetchInitialValues()
        if (response.isSuccessful) {
            if (response.isSuccessful) {
                Log.e("Success Rawinder01", Gson().toJson(response.body()))
            } else Log.e("UnSuccess Rawinder01", Gson().toJson(response.errorBody()))
            apiResultInitialValues.postValue(
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
            apiResultInitialValues.postValue(
                ApiResult.Error(
                    errorMsg,
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
        }

    }
}