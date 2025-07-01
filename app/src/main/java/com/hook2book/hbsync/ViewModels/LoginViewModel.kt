package com.sikhreader.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hook2book.hbsync.Model.Token.Token
import com.sikhreader.EnumClasses.ApiResult
import com.sikhreader.Model.SellerData.SellerData
import com.sikhreader.Repository.CommonLoginRepo
import com.sikhreader.Repository.SellerDataFormRepo
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val commonLoginRepository: CommonLoginRepo = CommonLoginRepo()
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