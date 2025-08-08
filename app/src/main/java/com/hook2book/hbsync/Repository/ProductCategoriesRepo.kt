package com.hook2book.hbsync.Repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hook2book.hbsync.EnumClasses.ApiResult
import com.hook2book.hbsync.Interface.RetroService
import com.hook2book.hbsync.Model.Categories.CategoriesMain
import com.hook2book.hbsync.RoomDatabase.CategoryEntity
import com.hook2book.hbsync.UtilityClass.Preferences
import com.hook2book.roomdb1.AppDatabase

class ProductCategoriesRepo(application: Application) {
    var apiResult: MutableLiveData<ApiResult<List<CategoriesMain>>> = MutableLiveData()
    var localCategoriesResult: MutableLiveData<List<CategoryEntity>> = MutableLiveData()
    var application: Application = application
    var db = AppDatabase.getDatabase(application)
    var dao = db.categoryDao()


    suspend fun fetchCategories() {
        val params = HashMap<String, String>()
        params.put("per_page", "100")
        val response = RetroService.retroInstance.fetchCategories(
            Preferences.loadCombinedKey(application),
            params
        )
        if (response.isSuccessful) {
            if (response.isSuccessful) {
                Log.e("Success Rawinder1901", Gson().toJson(response.body()))
            } else Log.e("UnSuccess Rawinder1901", Gson().toJson(response.errorBody()))
            apiResult.postValue(
                ApiResult.Success(
                    response.body(),
                    response.headers().get("X-WP-Total"),
                    response.headers().get("X-WP-TotalPages")
                )
            )
            Log.i("TAG", "fetchParentCategories: " + response.body())

        } else {
            val errorMsg = response.errorBody()?.string()
            if (response.isSuccessful) {
                Log.e("Success Rawinder1901", Gson().toJson(response.body()))
            } else Log.e("UnSuccess Rawinder1901", Gson().toJson(response.errorBody()))
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

    suspend fun getLocalCategories() {
        localCategoriesResult.postValue(dao.getAllCategories())
    }
}