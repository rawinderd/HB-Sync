package com.hook2book.hbsync.Repository

import android.app.Application
import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.hook2book.hbsync.EnumClasses.ApiResult
import com.hook2book.hbsync.Interface.RetroService
import com.hook2book.hbsync.Model.Token.Token

import java.nio.charset.StandardCharsets

class CommonLoginRepo(application: Application) {
    var apiResultLogin: MutableLiveData<ApiResult<Token>> = MutableLiveData()
    var application: Application = application
    suspend fun allowAccess(username: String, password: String) {

        val jsonObject = JsonObject()
        jsonObject.addProperty("username", username)
        jsonObject.addProperty("password", password)
        Log.i("TAG", "allowAccess: " + Gson().toJson(jsonObject))
        val response = RetroService.retroInstance.login(jsonObject)
        if (response.isSuccessful) {
            if (response.isSuccessful) {
                Log.e("Success Rawinder01", Gson().toJson(response.body()))
            } else Log.e("UnSuccess Rawinder01", Gson().toJson(response.errorBody()))
            apiResultLogin.postValue(
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
            apiResultLogin.postValue(
                ApiResult.Error(
                    errorMsg,
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
        }
    }
}