package com.sikhreader.Repository

import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.hook2book.hbsync.Model.Token.Token
import com.sikhreader.EnumClasses.ApiResult
import com.sikhreader.Interface.RetroService
import java.nio.charset.StandardCharsets

class CommonLoginRepo {
    var apiResultLogin: MutableLiveData<ApiResult<Token>> = MutableLiveData()
    suspend fun allowAccess(username: String, password: String) {
        val ck = "ck_7fdbe299e87f8c839402fa4720538e2c948435aa"
        val cs = "cs_74da2c5beb91a60993eb8977d86e307e075a854f"
        val base = ck + ":" + cs
        val authHeader = "Basic " + Base64.encodeToString(
            base.toByteArray(StandardCharsets.UTF_8), Base64.NO_WRAP
        )
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