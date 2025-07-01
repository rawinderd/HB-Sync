package com.hook2book.hbsync.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import com.hook2book.hbsync.R
import com.hook2book.hbsync.UtilityClass.BaseActivity
import com.hook2book.hbsync.databinding.ActivityProductPriceBinding

class ProductPrice : BaseActivity() {
    private lateinit var binding: ActivityProductPriceBinding
    private lateinit var taxStatusSpinner: Spinner
    private lateinit var taxClassSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductPriceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: Toolbar = findViewById(R.id.toolbar_simple)
        setToolbar(toolbar, true, "Price", false)
        if (intent.hasExtra("regularPrice")) {
            binding.regularPriceInputInner.setText(intent.getStringExtra("regularPrice"))
        }
        if (intent.hasExtra("salePrice")) {
            binding.salePriceInputInner.setText(intent.getStringExtra("salePrice"))
        }
        binding.saleSwitch.isChecked = false
        binding.saleToDateOuter.visibility = View.GONE
        binding.saleFromDateOuter.visibility = View.GONE
        binding.saleSwitch.setOnClickListener(View.OnClickListener {
            if (binding.saleSwitch.isChecked) {
                binding.saleToDateOuter.visibility = View.VISIBLE
                binding.saleFromDateOuter.visibility = View.VISIBLE
            } else {
                binding.saleToDateOuter.visibility = View.GONE
                binding.saleFromDateOuter.visibility = View.GONE
            }
        })
        val taxStatus = resources.getStringArray(R.array.taxStatus)
        taxStatusSpinner = findViewById(R.id.tax_status_spinner)
        if (taxStatusSpinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, taxStatus)
            taxStatusSpinner.adapter = adapter
        }
        val taxClass = resources.getStringArray(R.array.taxClass)
        taxClassSpinner = findViewById(R.id.tax_class_spinner)
        if (taxClassSpinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, taxClass)
            taxClassSpinner.adapter = adapter
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                println("Back button pressed")
                // Code that you need to execute on back press, e.g. finish()
                val returnIntent = Intent()
                returnIntent.putExtra("activityName", "ProductPrice")
                if (binding.regularPriceInputInner.text.toString().isNotEmpty()) {
                    returnIntent.putExtra(
                        "regularPrice", binding.regularPriceInputInner.text.toString()
                    )
                    if (binding.salePriceInputInner.text.toString().isNotEmpty()) {
                        if ((binding.salePriceInputInner.text.toString()).toInt() > 0) {
                            if ((binding.regularPriceInputInner.text.toString()).toInt() < (binding.salePriceInputInner.text.toString()).toInt()) {
                                Toasti("Regular Price Can't be Less than Sale Price")
                                Log.i(
                                    "TAG",
                                    "onBackPressed:price " + binding.regularPriceInputInner.text.toString()
                                )
                                Log.i(
                                    "TAG",
                                    "onBackPressed:price " + binding.salePriceInputInner.text.toString()
                                )
                            } else {
                                returnIntent.putExtra(
                                    "salePrice", binding.salePriceInputInner.text.toString()
                                )
                                //  super.onBackPressed()
                                setResult(Activity.RESULT_OK, returnIntent)
                                finish()
                            }
                        } else {
                            Toasti("Sale Price Can't be Zero")
                        }
                    } else {
                        returnIntent.putExtra("salePrice", "empty")
                        //super.onBackPressed()
                        setResult(Activity.RESULT_OK, returnIntent)
                        finish()
                    }
                } else {
                    if (binding.regularPriceInputInner.text.toString().isEmpty()) {
                        Toasti("Regular Price must be Added")
                    }
                }
            }
        })
    }

    /*override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Toasti("Back Pressed")
        val returnIntent = Intent()
        returnIntent.putExtra("activityName", "ProductPrice")
        if (binding.regularPriceInputInner.text.toString().isNotEmpty()) {
            returnIntent.putExtra("regularPrice", binding.regularPriceInputInner.text.toString())
            if (binding.salePriceInputInner.text.toString().isNotEmpty()) {
                if((binding.salePriceInputInner.text.toString()).toInt()>0) {
                    if ((binding.regularPriceInputInner.text.toString()).toInt() < (binding.salePriceInputInner.text.toString()).toInt()) {
                        Toasti("Regular Price Can't be Less than Sale Price")
                        Log.i("TAG", "onBackPressed:price "+binding.regularPriceInputInner.text.toString())
                        Log.i("TAG", "onBackPressed:price "+binding.salePriceInputInner.text.toString())
                    } else {
                        returnIntent.putExtra(
                            "salePrice",
                            binding.salePriceInputInner.text.toString()
                        )
                        super.onBackPressed()
                        setResult(Activity.RESULT_OK, returnIntent)
                        finish()
                    }
                }
                else{
                    Toasti("Sale Price Can't be Zero")
                }
            } else {
                returnIntent.putExtra("salePrice", "empty")
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        } else {
            if (binding.regularPriceInputInner.text.toString().isEmpty()) {
                Toasti("Regular Price must be Added")
            }
        }
        return super.onKeyDown(keyCode, event)
    }*/

    override fun onBackPressed() {
        // super.onBackPressed()
        val returnIntent = Intent()
        returnIntent.putExtra("activityName", "ProductPrice")
        if (binding.regularPriceInputInner.text.toString().isNotEmpty()) {
            returnIntent.putExtra("regularPrice", binding.regularPriceInputInner.text.toString())
            if (binding.salePriceInputInner.text.toString().isNotEmpty()) {
                if ((binding.salePriceInputInner.text.toString()).toInt() > 0) {
                    if ((binding.regularPriceInputInner.text.toString()).toInt() < (binding.salePriceInputInner.text.toString()).toInt()) {
                        Toasti("Regular Price Can't be Less than Sale Price")
                        Log.i(
                            "TAG",
                            "onBackPressed:price " + binding.regularPriceInputInner.text.toString()
                        )
                        Log.i(
                            "TAG",
                            "onBackPressed:price " + binding.salePriceInputInner.text.toString()
                        )
                    } else {
                        returnIntent.putExtra(
                            "salePrice", binding.salePriceInputInner.text.toString()
                        )
                        super.onBackPressed()
                        setResult(Activity.RESULT_OK, returnIntent)
                        finish()
                    }
                } else {
                    Toasti("Sale Price Can't be Zero")
                }
            } else {
                returnIntent.putExtra("salePrice", "empty")
                super.onBackPressed()
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        } else {
            if (binding.regularPriceInputInner.text.toString().isEmpty()) {
                Toasti("Regular Price must be Added")
            }
        }
    }
}