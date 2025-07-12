package com.hook2book.hbsync.Activities

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.hook2book.hbsync.Model.SellerData.SellerData
import com.hook2book.hbsync.R
import com.hook2book.hbsync.UtilityClass.BaseActivity
import com.hook2book.hbsync.UtilityClass.Preferences
import com.hook2book.hbsync.databinding.ActivityAccountBinding

class AccountActivity : BaseActivity() {
    private lateinit var binding: ActivityAccountBinding
    private lateinit var toolbar: Toolbar
    private lateinit var sellerData: SellerData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbar = findViewById(R.id.toolbar_simple)
        setToolbar(toolbar, true, "Manage Account", true)
        sellerData = Preferences.loadAccountInfo(applicationContext)
        loadDataInto(sellerData)
    }

    private fun loadDataInto(sellerData: SellerData?) {
        if (sellerData != null) {
            binding.loginNameIn.setText(sellerData.data_outer.get(0).name)
            binding.loginAddressIn.setText(sellerData.data_outer.get(0).address)
            binding.loginFirmNameIn.setText(sellerData.data_outer.get(0).firm_name)
            binding.loginPhoneNumberIn.setText(sellerData.data_outer.get(0).mob_number)
            if (sellerData.data_outer.get(0).user_type == 1) {
                binding.radioPublisherAccount.isChecked = true
            } else if (sellerData.data_outer.get(0).user_type == 2) {
                binding.radioSellerAccount.isChecked = true
            } else if (sellerData.data_outer.get(0).user_type == 3) {
                binding.radioWriterAccount.isChecked = true
            }
        }

    }
}