package com.hook2book.hbsync.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hook2book.hbsync.EnumClasses.ApiResult
import com.hook2book.hbsync.Model.Registration.RegistrationData
import com.hook2book.hbsync.Model.Registration.RegistrationResponse
import com.hook2book.hbsync.Model.Token.Token
import com.hook2book.hbsync.Repository.CommonLoginRepo
import com.hook2book.hbsync.Repository.RegisterRepo

import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val registerRepo: RegisterRepo = RegisterRepo(application)
    private val commonLoginRepository: CommonLoginRepo = CommonLoginRepo(application)

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