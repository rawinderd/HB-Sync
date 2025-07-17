package com.hook2book.hbsync.Activities

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.hook2book.hbsync.EnumClasses.ApiStatus
import com.hook2book.hbsync.Model.ViewModels.SplashViewModel
import com.hook2book.hbsync.UtilityClass.BaseActivity
import com.hook2book.hbsync.UtilityClass.Preferences
import com.hook2book.hbsync.UtilityClass.Prevalent
import com.hook2book.hbsync.databinding.ActivitySplashBinding
import io.paperdb.Paper
import java.nio.charset.StandardCharsets

class Splash : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var splashViewModel: SplashViewModel
    private lateinit var UserPhoneKey: String
    private lateinit var UserPasswordKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        init()
        splashViewModel.fetchInitialValues()
        splashViewModel.getInitialValues().observe(this) {
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    var ck: String? = null
                    var cs: String? = null
                    val base: String
                    val authHeader: String
                    for (i in 0..it.data?.data?.size!! - 1) {
                        if (it.data.data!!.get(i).name.equals("consumer_key")) {
                            ck = it.data.data!!.get(i).value
                        }
                        if (it.data.data!!.get(i).name.equals("consumer_secret")) {
                            cs = it.data.data!!.get(i).value
                        }
                    }
                    base = "$ck:$cs"
                    authHeader = "Basic " + android.util.Base64.encodeToString(
                        base.toByteArray(StandardCharsets.UTF_8), android.util.Base64.NO_WRAP
                    )
                    Preferences.saveCombinedKey(applicationContext, authHeader)
                    if (UserPhoneKey.equals("null") && UserPasswordKey.equals("null")) {
                        val intent = Intent(this, RegisterActivity::class.java)
                        startActivity(intent)
                    } else {
                        splashViewModel.allowAccess(UserPhoneKey, UserPasswordKey)
                    }
                }
                ApiStatus.ERROR -> {
                    Toasti("Data Not Fetched Correctly")
                }

                ApiStatus.LOADING -> TODO()
            }
        }
        splashViewModel.getLoginResponse().observe(this) {
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    Paper.book().write(Prevalent.PubId, it.data?.wpUser?.data?.id.toString())

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("password", Paper.book().read<String>(Prevalent.UserPasswordKey).toString())
                    intent.putExtra("userId", it.data?.wpUser?.id.toString())
                    startActivity(intent)
                }
                ApiStatus.ERROR -> Toasti("Error In Login")
                ApiStatus.LOADING -> TODO()
            }
        }
    }

    fun init() {
        Paper.init(applicationContext)
        UserPhoneKey = Paper.book().read<String>(Prevalent.UserPhoneKey).toString()
        UserPasswordKey = Paper.book().read<String>(Prevalent.UserPasswordKey).toString()
    }
}