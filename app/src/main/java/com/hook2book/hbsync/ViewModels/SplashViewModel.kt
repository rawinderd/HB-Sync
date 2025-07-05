package com.hook2book.hbsync.Model.ViewModels
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hook2book.hbsync.EnumClasses.ApiResult
import com.hook2book.hbsync.Model.InitialVaribales.InitialVariablesMain
import com.hook2book.hbsync.Model.Token.Token
import com.hook2book.hbsync.Repository.CommonLoginRepo
import com.hook2book.hbsync.Repository.SplashRepo
import kotlinx.coroutines.launch

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    private var commonLoginRepo: CommonLoginRepo = CommonLoginRepo(application)
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

