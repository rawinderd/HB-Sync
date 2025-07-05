package com.hook2book.hbsync.Repository
import android.app.Application
import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hook2book.hbsync.EnumClasses.ApiResult
import com.hook2book.hbsync.Interface.RetroService
import com.hook2book.hbsync.Model.NewTag.newTag
import com.hook2book.hbsync.Model.ProductTags.ProductTagsMain
import com.hook2book.hbsync.UtilityClass.Preferences

import java.nio.charset.StandardCharsets

class ProductsTagsRepo(application: Application) {
    var apiResult: MutableLiveData<ApiResult<List<ProductTagsMain>>> = MutableLiveData()
    var apiResultLanguageTag: MutableLiveData<ApiResult<List<ProductTagsMain>>> = MutableLiveData()
    var apiResultCategoryTag: MutableLiveData<ApiResult<List<ProductTagsMain>>> = MutableLiveData()
    var apiResultNewTag: MutableLiveData<ApiResult<newTag>> = MutableLiveData()
    var application: Application = application
    suspend fun getTagList(keyword: String, pageNumber: Int) {
        val params = HashMap<String, String>()
        if (keyword != "None") {
               params.put("search",keyword)
        }
        params.put("per_page", "32")
        params.put("page", pageNumber.toString())

        val response = RetroService.retroInstance.getTagList(Preferences.loadCombinedKey(application), params)
        if (response.isSuccessful) {
            Log.e("Success Rawinder501", Gson().toJson(response.body()))
        } else Log.e("UnSuccess Rawinder501", Gson().toJson(response.errorBody()))
        if (response.isSuccessful) {
            apiResult.postValue(
                ApiResult.Success(
                    response.body(),
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )

        } else {
            val errorMsg = response.errorBody()?.string()
            response.errorBody()
                ?.close()  // remember to close it after getting the stream of error body
            apiResult.postValue(
                ApiResult.Error(
                    errorMsg,
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
        }
    }

    suspend fun createNewTag(newTagText: String) {
        val params = HashMap<String, String>()
        params.put("name", newTagText)
        val response = RetroService.retroInstance.createNewTag(Preferences.loadCombinedKey(application), params)
        if (response.isSuccessful) {
            Log.e("Success Rawinder502", Gson().toJson(response.body()))
        } else Log.e("UnSuccess Rawinder502", Gson().toJson(response.errorBody()))
        if (response.isSuccessful) {
            apiResultNewTag.postValue(
                ApiResult.Success(
                    response.body(),
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )

        } else {
            val errorMsg = response.errorBody()?.string()
            response.errorBody()
                ?.close()  // remember to close it after getting the stream of error body
            apiResultNewTag.postValue(
                ApiResult.Error(
                    errorMsg,
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
        }
    }
    suspend fun getTagListLanguage(keyword: String, pageNumber: Int) {
        val params = HashMap<String, String>()
        if (keyword != "None") {
            params.put("search",keyword)
        }
        params.put("per_page", "32")
        params.put("page", pageNumber.toString())

        val response = RetroService.retroInstance.getTagList(Preferences.loadCombinedKey(application), params)
        if (response.isSuccessful) {
            Log.e("Success Rawinder501", Gson().toJson(response.body()))
        } else Log.e("UnSuccess Rawinder501", Gson().toJson(response.errorBody()))
        if (response.isSuccessful) {
            apiResultLanguageTag.postValue(
                ApiResult.Success(
                    response.body(),
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )

        } else {
            val errorMsg = response.errorBody()?.string()
            response.errorBody()
                ?.close()  // remember to close it after getting the stream of error body
            apiResultLanguageTag.postValue(
                ApiResult.Error(
                    errorMsg,
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
        }
    }
    suspend fun getTagListCategory(keyword: String, pageNumber: Int) {
        val params = HashMap<String, String>()
        if (keyword != "None") {
            params.put("search",keyword)
        }
        params.put("per_page", "32")
        params.put("page", pageNumber.toString())

        val response = RetroService.retroInstance.getTagList(Preferences.loadCombinedKey(application), params)
        if (response.isSuccessful) {
            Log.e("Success Rawinder501", Gson().toJson(response.body()))
        } else Log.e("UnSuccess Rawinder501", Gson().toJson(response.errorBody()))
        if (response.isSuccessful) {
            apiResultCategoryTag.postValue(
                ApiResult.Success(
                    response.body(),
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )

        } else {
            val errorMsg = response.errorBody()?.string()
            response.errorBody()
                ?.close()  // remember to close it after getting the stream of error body
            apiResultCategoryTag.postValue(
                ApiResult.Error(
                    errorMsg,
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
        }
    }
}