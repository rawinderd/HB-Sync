package com.hook2book.hbsync.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hook2book.hbsync.EnumClasses.ApiResult
import com.hook2book.hbsync.Model.SellerData.SellerData
import com.hook2book.hbsync.Model.Token.Token
import com.hook2book.hbsync.Repository.CommonLoginRepo
import com.hook2book.hbsync.Repository.SellerDataFormRepo

import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val commonLoginRepository: CommonLoginRepo = CommonLoginRepo(application)
    private var sellerDataFormRepo: SellerDataFormRepo = SellerDataFormRepo()
    fun fetchUserData(pubId: String) {
        viewModelScope.launch {
            sellerDataFormRepo.fetchUserData(pubId)
        }
    }

    fun getUserData(): MutableLiveData<ApiResult<SellerData>> {
        return sellerDataFormRepo.apiSellerDataResponse
    }

    fun allowAccess(username: String, password: String) {
        viewModelScope.launch {
            commonLoginRepository.allowAccess(username, password)
        }
    }

    fun getLoginResponse(): LiveData<ApiResult<Token>> {
        return commonLoginRepository.apiResultLogin

    }
}