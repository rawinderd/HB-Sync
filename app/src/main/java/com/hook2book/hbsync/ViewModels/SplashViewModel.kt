package com.sikhreader.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hook2book.hbsync.Model.InitialVaribales.InitialVariablesMain
import com.hook2book.hbsync.Model.Token.Token
import com.hook2book.hbsync.Repository.SplashRepo
import com.sikhreader.EnumClasses.ApiResult
import com.sikhreader.Repository.CommonLoginRepo
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    private var commonLoginRepo: CommonLoginRepo = CommonLoginRepo()
    private var splashRepo: SplashRepo = SplashRepo()

    fun fetchInitialValues() {
        viewModelScope.launch {
            splashRepo.fetchInitialValues()
        }
    }

    fun getInitialValues(): MutableLiveData<ApiResult<InitialVariablesMain>> {
        return splashRepo.apiResultInitialValues
    }

    fun allowAccess(username: String, password: String) {
        viewModelScope.launch {
            commonLoginRepo.allowAccess(username, password)
        }
    }

    fun getLoginResponse(): LiveData<ApiResult<Token>> {
        return commonLoginRepo.apiResultLogin

    }
}

