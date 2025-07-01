package com.sikhreader.Repository

import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sikhreader.EnumClasses.ApiResult
import com.sikhreader.Interface.RetroService
import com.sikhreader.Model.Registration.RegistrationData
import com.sikhreader.Model.Registration.RegistrationResponse
import java.nio.charset.StandardCharsets

class RegisterRepo {
    var apiResultRegistration: MutableLiveData<ApiResult<RegistrationResponse>> = MutableLiveData()
    suspend fun registerUserAccount(registrationData: RegistrationData) {
        val ck = "ck_7fdbe299e87f8c839402fa4720538e2c948435aa"
        val cs = "cs_74da2c5beb91a60993eb8977d86e307e075a854f"
        val base = ck + ":" + cs
        val authHeader = "Basic " + Base64.encodeToString(
            base.toByteArray(StandardCharsets.UTF_8), Base64.NO_WRAP
        )
        val response = RetroService.retroInstance.registerUserAccount(authHeader, registrationData)
        if (response.isSuccessful) {
            if (response.isSuccessful) {
                Log.e("Success Rawinder01", Gson().toJson(response.body()))
            } else Log.e("UnSuccess Rawinder01", Gson().toJson(response.errorBody()))
            apiResultRegistration.postValue(
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
            apiResultRegistration.postValue(
                ApiResult.Error(
                    errorMsg,
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
        }
    }
}