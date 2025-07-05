package com.hook2book.hbsync.Activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.hook2book.hbsync.Activities.SellerDataForm
import com.hook2book.hbsync.EnumClasses.ApiStatus
import com.hook2book.hbsync.Model.Registration.Billing
import com.hook2book.hbsync.Model.Registration.RegistrationData
import com.hook2book.hbsync.Model.Registration.Shipping
import com.hook2book.hbsync.R
import com.hook2book.hbsync.UtilityClass.AppUtils
import com.hook2book.hbsync.UtilityClass.BaseActivity
import com.hook2book.hbsync.UtilityClass.Prevalent
import com.hook2book.hbsync.ViewModels.RegisterViewModel
import com.hook2book.hbsync.databinding.ActivityRegisterBinding



import io.paperdb.Paper

class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var CreateAccountButton: Button
    private lateinit var InputPhoneNumber: EditText
    private lateinit var InputPassword: EditText
    private lateinit var InputEmail: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        registerViewModel.getRegisterUserResponse().observe(this) {
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    Toasti("User Registered Successfully")
                    registerViewModel.allowAccess(
                        InputPhoneNumber.text.toString(), InputPassword.text.toString()
                    )
                }

                ApiStatus.ERROR -> {
                    Toasti(it.message)
                }

                ApiStatus.LOADING -> TODO()
            }
        }
        registerViewModel.getLoginResponse().observe(this) {
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    Toasti("User Login Successfully " + (it.data?.wpUser?.id))
                    Paper.book().write(Prevalent.UserPhoneKey, InputPhoneNumber.text.toString())
                    Paper.book().write(Prevalent.UserPasswordKey, InputPassword.text.toString())
                    Paper.book().write(Prevalent.PubId, it.data?.wpUser?.id.toString())
                    val intent = Intent(this, SellerDataForm::class.java)
                    intent.putExtra("mobileNo", InputPhoneNumber.text.toString())
                    intent.putExtra("password", InputPassword.text.toString())
                    intent.putExtra("userId", it.data?.wpUser?.id.toString())
                    startActivity(intent)
                }

                ApiStatus.ERROR -> {
                    Toasti(it.message)
                }

                ApiStatus.LOADING -> TODO()
            }
        }
        binding.signInText.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        })

        CreateAccountButton.setOnClickListener {
            val registrationData: RegistrationData
            val email = InputEmail.text.toString().trim { it <= ' ' }
            val phone = InputPhoneNumber.text.toString()
            val password = InputPassword.text.toString()
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_LONG).show()
            } else if (TextUtils.isEmpty(phone)) {
                Toast.makeText(
                    this, "Please Enter Your Mobile Number", Toast.LENGTH_LONG
                ).show()
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(
                    this, "Please Enter Your Password", Toast.LENGTH_LONG
                ).show()
            } else if (!AppUtils.isEmailValid(email)) {
                Toast.makeText(
                    this, " Email Address Not Valid", Toast.LENGTH_SHORT
                ).show()
            } else {
                registrationData = RegistrationData(
                    email, "First_Name", "Last_Name", password, phone, Billing(), Shipping()
                )
                registerViewModel.registerUserAccount(registrationData)
            }
        }
    }

    private fun initComponents() {
        CreateAccountButton = findViewById(R.id.register_btn)
        InputPhoneNumber = findViewById(R.id.register_phone_number_input)
        InputPassword = findViewById(R.id.register_password_input)
        InputEmail = findViewById(R.id.register_email_input)
    }
}