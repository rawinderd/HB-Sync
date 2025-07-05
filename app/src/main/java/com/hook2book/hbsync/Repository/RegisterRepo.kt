package com.hook2book.hbsync.Repository

import android.app.Application
import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hook2book.hbsync.EnumClasses.ApiResult
import com.hook2book.hbsync.Interface.RetroService
import com.hook2book.hbsync.Model.Registration.RegistrationData
import com.hook2book.hbsync.Model.Registration.RegistrationResponse
import com.hook2book.hbsync.UtilityClass.Preferences

import java.nio.charset.StandardCharsets

class RegisterRepo(application: Application) {
    var apiResultRegistration: MutableLiveData<ApiResult<RegistrationResponse>> = MutableLiveData()
    var application: Application = application
    suspend fun registerUserAccount(registrationData: RegistrationData) {

        val response = RetroService.retroInstance.registerUserAccount(Preferences.loadCombinedKey(application), registrationData)
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