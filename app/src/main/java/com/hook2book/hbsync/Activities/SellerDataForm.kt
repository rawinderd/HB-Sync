package com.hook2book.hbsync.Activities

import android.R
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.hook2book.hbsync.Model.SellerData.DataOuter
import com.hook2book.hbsync.Model.ViewModels.SellerDataFormViewModel
import com.hook2book.hbsync.UtilityClass.BaseActivity
import com.hook2book.hbsync.databinding.ActivitySellerDataFormBinding


class SellerDataForm : BaseActivity() {
    private lateinit var binding: ActivitySellerDataFormBinding
    private lateinit var mobileNo: String
    private lateinit var wcId: String
    private lateinit var password: String
    private lateinit var sellerData: DataOuter
    private lateinit var name: String
    private lateinit var firmName: String
    private lateinit var sellerDataFormViewModel: SellerDataFormViewModel
    private lateinit var address: String
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButton: RadioButton
    private var userType = 1 // Default value for seller type
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerDataFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //radioGroup=findViewById(R.id.radio_group_seller_type)
        sellerDataFormViewModel = ViewModelProvider(this).get(SellerDataFormViewModel::class.java)
        if (intent.hasExtra("mobileNo")) {
            mobileNo = intent.getStringExtra("mobileNo").toString()
            wcId = intent.getStringExtra("userId").toString()
            password = intent.getStringExtra("password").toString()
            binding.loginPhoneNumberInner.setText(mobileNo)
        }
        binding.submitBtnSellerData.setOnClickListener(View.OnClickListener {
            name = binding.loginNameInnerr.text.toString()
            mobileNo = binding.loginPhoneNumberInner.text.toString()
            firmName = binding.loginFirmNameInput.text.toString()
            address = binding.loginAddressInner.text.toString()
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_LONG).show()
            } else {
                if (TextUtils.isEmpty(firmName)) {
                    Toast.makeText(
                        this, "Please Enter Firm Name/Publication/Page Name", Toast.LENGTH_LONG
                    ).show()
                } else {
                    if (TextUtils.isEmpty(mobileNo)) {
                        Toast.makeText(this, "Please Enter Mobile No.", Toast.LENGTH_LONG).show()
                    } else {
                        if (TextUtils.isEmpty(address)) {
                            Toast.makeText(this, "Please Enter Address", Toast.LENGTH_LONG).show()
                        } else {
                            //Toasti("Data Complete")
                            sellerData = DataOuter(
                                name,
                                mobileNo,
                                firmName,
                                address,
                                wcId,
                                password,
                                1, "1111",
                                1,
                            )
                            sellerDataFormViewModel.insertSellerData(sellerData)
                        }
                    }
                }
            }
        })
        binding.radioGroupSellerType.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            // checkedId is the RadioButton selected
            radioButton = findViewById<View>(checkedId) as RadioButton
            if( radioButton.text.toString() == "Writer") {
                userType = 3
            } else if (radioButton.text.toString() == "Publisher") {
                userType = 1
            } else if (radioButton.text.toString() == "Seller") {
                userType = 2
            }
            //Toast.makeText(this, "Selected: " + radioButton.text, Toast.LENGTH_SHORT).show()
        })
        sellerDataFormViewModel.sellerDataStatus().observe(this) {
            //Toasti("msg is " + it.data?.message.toString())
            if (it.data?.status.equals("success")) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toasti("Error is " + it.status.toString())
            }
        }
    }

   /* fun addListenerOnButton() {
        radioGroup = findViewById<View>(R.id.radio_group_seller_type) as RadioGroup
        //btnDisplay = findViewById<View>(R.id.btnDisplay) as Button

        // btnDisplay.setOnClickListener(object : OnClickListener() {
        override fun onClick(v: View?) {
            // get selected radio button from radioGroup

            val selectedId: Int = binding.radioGroupSellerType.checkedRadioButtonId
            // find the radiobutton by returned id
            radioButton = findViewById<View>(selectedId) as RadioButton
            Toasti(radioButton.text.toString())
        }
    })*/

}