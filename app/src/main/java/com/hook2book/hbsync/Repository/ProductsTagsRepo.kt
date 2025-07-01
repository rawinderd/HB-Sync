package com.sikhreader.Repository

import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.ProductTagsMain
import com.google.gson.Gson
import com.sikhreader.EnumClasses.ApiResult
import com.sikhreader.Interface.RetroService
import com.sikhreader.Model.NewTag.newTag
import java.nio.charset.StandardCharsets

class ProductsTagsRepo {
    var apiResult: MutableLiveData<ApiResult<List<ProductTagsMain>>> = MutableLiveData()
    var apiResultLanguageTag: MutableLiveData<ApiResult<List<ProductTagsMain>>> = MutableLiveData()
    var apiResultCategoryTag: MutableLiveData<ApiResult<List<ProductTagsMain>>> = MutableLiveData()
    var apiResultNewTag: MutableLiveData<ApiResult<newTag>> = MutableLiveData()
    suspend fun getTagList(keyword: String, pageNumber: Int) {
        val ck = "ck_7fdbe299e87f8c839402fa4720538e2c948435aa"
        val cs = "cs_74da2c5beb91a60993eb8977d86e307e075a854f"
        val base = ck + ":" + cs
        val authHeader = "Basic " + Base64.encodeToString(
            base.toByteArray(StandardCharsets.UTF_8),
            Base64.NO_WRAP)
        val params = HashMap<String, String>()
        if (keyword != "None") {
               params.put("search",keyword)
        }
        params.put("per_page", "32")
        params.put("page", pageNumber.toString())

        val response = RetroService.retroInstance.getTagList(authHeader, params)
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
        val ck = "ck_7fdbe299e87f8c839402fa4720538e2c948435aa"
        val cs = "cs_74da2c5beb91a60993eb8977d86e307e075a854f"
        val base = ck + ":" + cs
        val authHeader = "Basic " + Base64.encodeToString(
            base.toByteArray(StandardCharsets.UTF_8),
            Base64.NO_WRAP)
        Log.i("TAG", "createNewTag: "+newTagText)
        val params = HashMap<String, String>()
        params.put("name", newTagText)
        val response = RetroService.retroInstance.createNewTag(authHeader, params)
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
        val ck = "ck_7fdbe299e87f8c839402fa4720538e2c948435aa"
        val cs = "cs_74da2c5beb91a60993eb8977d86e307e075a854f"
        val base = ck + ":" + cs
        val authHeader = "Basic " + Base64.encodeToString(
            base.toByteArray(StandardCharsets.UTF_8),
            Base64.NO_WRAP)
        val params = HashMap<String, String>()
        if (keyword != "None") {
            params.put("search",keyword)
        }
        params.put("per_page", "32")
        params.put("page", pageNumber.toString())

        val response = RetroService.retroInstance.getTagList(authHeader, params)
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
        val ck = "ck_7fdbe299e87f8c839402fa4720538e2c948435aa"
        val cs = "cs_74da2c5beb91a60993eb8977d86e307e075a854f"
        val base = ck + ":" + cs
        val authHeader = "Basic " + Base64.encodeToString(
            base.toByteArray(StandardCharsets.UTF_8),
            Base64.NO_WRAP)
        val params = HashMap<String, String>()
        if (keyword != "None") {
            params.put("search",keyword)
        }
        params.put("per_page", "32")
        params.put("page", pageNumber.toString())

        val response = RetroService.retroInstance.getTagList(authHeader, params)
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