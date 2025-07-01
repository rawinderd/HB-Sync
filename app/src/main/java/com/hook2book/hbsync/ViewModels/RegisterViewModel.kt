package com.sikhreader.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hook2book.hbsync.Model.Token.Token
import com.sikhreader.EnumClasses.ApiResult
import com.sikhreader.Model.Registration.RegistrationData
import com.sikhreader.Model.Registration.RegistrationResponse
import com.sikhreader.Repository.CommonLoginRepo
import com.sikhreader.Repository.RegisterRepo
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val registerRepo: RegisterRepo = RegisterRepo()
    private val commonLoginRepository: CommonLoginRepo = CommonLoginRepo()

    fun registerUserAccount(registrationData: RegistrationData) {
        viewModelScope.launch {
            registerRepo.registerUserAccount(registrationData)
        }
    }

    fun getRegisterUserResponse(): LiveData<ApiResult<RegistrationResponse>> {
        return registerRepo.apiResultRegistration

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