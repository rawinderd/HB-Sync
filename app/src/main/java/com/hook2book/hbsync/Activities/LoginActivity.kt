package com.hook2book.hbsync.Activities

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.hook2book.hbsync.UtilityClass.BaseActivity
import com.hook2book.hbsync.UtilityClass.Prevalent
import com.hook2book.hbsync.databinding.ActivityLoginBinding
import com.sikhreader.EnumClasses.ApiStatus
import com.sikhreader.ViewModels.LoginViewModel
import io.paperdb.Paper
import org.json.JSONException

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private var mLastClickTime: Long = 0
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        Paper.init(applicationContext)
        binding.loginBtn.setOnClickListener(View.OnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@OnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            LoginUser()
        })
        loginViewModel.getLoginResponse().observe(this) {
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    Toasti("User Login Successfully")
                    Paper.book().write(
                        Prevalent.UserPhoneKey, binding.loginPhoneNumberInput.text.toString()
                    )
                    Paper.book().write(
                        Prevalent.UserPasswordKey, binding.loginPasswordInput.text.toString()
                    )
                    Paper.book().write(Prevalent.PubId, it.data?.wpUser?.data?.id.toString())
                    userId = it.data?.wpUser?.data?.id.toString()
                    loginViewModel.fetchUserData(it.data?.wpUser?.data?.id.toString())
                }

                ApiStatus.ERROR -> {
                    Toasti(it.message)
                }

                ApiStatus.LOADING -> TODO()
            }
        }
        loginViewModel.getUserData().observe(this) {
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    Toasti("User Data Fetched Successfully")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                ApiStatus.ERROR -> {
                    if (it.message.equals("{\"message\":\"Record not found\"}")) {
                        //Log.i("TAG", "onCreate: Please Fill Your Seller Data ${it.message}")
                        Toasti("Please Fill Your Seller Data Form")
                        val intent = Intent(this, SellerDataForm::class.java)
                        intent.putExtra("mobileNo", binding.loginPhoneNumberInput.text.toString())
                        intent.putExtra("password", binding.loginPasswordInput.text.toString())
                        intent.putExtra("userId", userId)
                        startActivity(intent)
                    } else {
                        Toasti(it.message)
                    }


                }
                ApiStatus.LOADING -> TODO()
            }
        }

    }

    private fun LoginUser() {
        val phone: String = binding.loginPhoneNumberInput.text.toString()
        val password: String = binding.loginPasswordInput.text.toString()
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please Enter Your Mobile Number", Toast.LENGTH_LONG).show()
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_LONG).show()
        } else {
            try {
                loginViewModel.allowAccess(phone, password)
            } catch (e: JSONException) {
                throw RuntimeException(e)
            }
        }
    }

    private fun initComponents() {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }
}