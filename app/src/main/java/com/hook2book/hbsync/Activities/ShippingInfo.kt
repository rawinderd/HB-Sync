package com.hook2book.hbsync.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.hook2book.hbsync.R
import com.hook2book.hbsync.UtilityClass.BaseActivity
import com.hook2book.hbsync.databinding.ActivityShippingInfoBinding
import com.sikhreader.ViewModels.ShippingViewModel

class ShippingInfo : BaseActivity() {
    private lateinit var binding: ActivityShippingInfoBinding
    lateinit var spinner: Spinner
    lateinit var shippingViewModel: ShippingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShippingInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar_simple)
        setToolbar(toolbar, true, "Shipping Info", false)
        val shippingClass = resources.getStringArray(R.array.Shipping)
        shippingViewModel = ViewModelProvider(this).get(ShippingViewModel::class.java)
        spinner = findViewById(R.id.shipping_class_spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, shippingClass)
        if (spinner != null) {
            spinner.adapter = adapter
        }
        if (intent.hasExtra("weight")) {
            binding.weightInputInner.setText(intent.getStringExtra("weight"))
        }
        if (intent.hasExtra("length")) {
            binding.lengthInputInner.setText(intent.getStringExtra("length"))
        }
        if (intent.hasExtra("width")) {
            binding.widthInputInner.setText(intent.getStringExtra("width"))
        }
        if (intent.hasExtra("height")) {
            binding.heightInputInner.setText(intent.getStringExtra("height"))
        }
        if (intent.hasExtra("shippingClass")) {
            val spinnderPosition = shippingClass.indexOf(intent.getStringExtra("shippingClass"))
            spinner.post(Runnable { spinner.setSelection(spinnderPosition) })
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val returnIntent = Intent()
                returnIntent.putExtra("activityName", "ShippingInfo")
                if (!binding.weightInputInner.text.toString().isEmpty()) {
                    returnIntent.putExtra(
                        "weight", binding.weightInputInner.text.toString()
                    )
                } else {
                    returnIntent.putExtra(
                        "weight", "empty"
                    )
                }
                if (!binding.lengthInputInner.text.toString().isEmpty()) {
                    returnIntent.putExtra(
                        "length", binding.lengthInputInner.text.toString()
                    )
                } else {
                    returnIntent.putExtra(
                        "length", "empty"
                    )
                }
                if (!binding.widthInputInner.text.toString().isEmpty()) {
                    returnIntent.putExtra(
                        "width", binding.widthInputInner.text.toString()
                    )
                } else {
                    returnIntent.putExtra(
                        "width", "empty"
                    )
                }
                if (!binding.heightInputInner.text.toString().isEmpty()) {
                    returnIntent.putExtra(
                        "height", binding.heightInputInner.text.toString()
                    )
                } else {
                    returnIntent.putExtra(
                        "height", "empty"
                    )
                }
                if (spinner.selectedItem.toString() != "None") {
                    returnIntent.putExtra(
                        "shippingClass", spinner.selectedItem.toString()
                    )
                } else {
                    returnIntent.putExtra(
                        "shippingClass", "empty"
                    )
                }
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        })


    }

    /*override fun onBackPressed() {
        super.onBackPressed()
        val returnIntent = Intent()
        returnIntent.putExtra("activityName", "ShippingInfo")
        if (!binding.weightInputInner.text.toString().isEmpty()) {
            returnIntent.putExtra(
                "weight", binding.weightInputInner.text.toString()
            )
        } else {
            returnIntent.putExtra(
                "weight", "empty"
            )
        }
        if (!binding.lengthInputInner.text.toString().isEmpty()) {
            returnIntent.putExtra(
                "length", binding.lengthInputInner.text.toString()
            )
        } else {
            returnIntent.putExtra(
                "length", "empty"
            )
        }
        if (!binding.widthInputInner.text.toString().isEmpty()) {
            returnIntent.putExtra(
                "width", binding.widthInputInner.text.toString()
            )
        } else {
            returnIntent.putExtra(
                "width", "empty"
            )
        }
        if (!binding.heightInputInner.text.toString().isEmpty()) {
            returnIntent.putExtra(
                "height", binding.heightInputInner.text.toString()
            )
        } else {
            returnIntent.putExtra(
                "height", "empty"
            )
        }
        if (spinner.getSelectedItem().toString() != "None") {
            returnIntent.putExtra(
                "shippingClass", spinner.getSelectedItem().toString()
            )
        } else {
            returnIntent.putExtra(
                "shippingClass", "empty"
            )
        }
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }*/
}