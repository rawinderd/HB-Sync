package com.hook2book.hbsync.ViewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.hook2book.hbsync.RoomDatabase.CategoriesMainForRoom
import kotlinx.coroutines.launch

class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {
    var application1: Application= application
    lateinit var fetchedlist: MutableLiveData<List<CategoriesMainForRoom>>

    init {
        this.application1 = application
    }

    fun clickCheck() {
        Log.i("TAG", "clickCheck: clicked")
    }


    fun insertCategories(list: List<CategoriesMainForRoom>) {

        viewModelScope.launch {
          //  CategoryDatabase.categoriesDao().insertAll(list)
        }
    }

    fun getAllCategories() {
        viewModelScope.launch {
          //  fetchedlist.postValue(CategoryDatabase.categoriesDao().getAll())
        }
    }
}